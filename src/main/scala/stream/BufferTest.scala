package stream

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Attributes, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

/**
  *
  * Created by ZJK on 2017/11/10.
  */
object BufferTest extends App {

  implicit val system = ActorSystem("BufferTest")
  implicit val m = ActorMaterializer()

  RunnableGraph.fromGraph(GraphDSL.create(){
    implicit b =>
      import GraphDSL.Implicits._
      import scala.concurrent.duration._
      val zipper = b.add(ZipWith[Tick, Int, Int]((_, count) => count).withAttributes(Attributes.inputBuffer(1,1)).async)

      Source.tick(initialDelay = 3.second, interval = 3.second, Tick()) ~> zipper.in0

      Source.tick(initialDelay = 1.second, interval = 1.second, "message!")
        .conflateWithSeed(seed=(y)=>{println(s"ssssf:$y"); 1})((count, x) => {println(s"sdfs:$x");count + 1}) ~> zipper.in1

      zipper.out ~> Sink.foreach(println)
     ClosedShape
  }).run()


  val f = (i: Int) => i.toString
  val g = (s: String) => s+s+s
  val h = g compose f  // : Int => String

}

case class Tick()


