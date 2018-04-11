package stream

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, UniformFanInShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.concurrent.Future

/**
  *
  * Created by ZJK on 2017/11/7.
  */
object OtherShape extends App {

  implicit val system = ActorSystem("OtherShape")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val pickMaxOfThree = GraphDSL.create(){
    implicit b =>
      import GraphDSL.Implicits._

      val zip1 = b.add(ZipWith[Int, Int, Int](math.max _))
      val zip2 = b.add(ZipWith[Int, Int, Int](math.max _))

      zip1.out ~> zip2.in0
      UniformFanInShape(zip2.out,zip1.in0, zip1.in1, zip2.in1)
  }


  val resultSink = Sink.foreach(println)
  val g = RunnableGraph.fromGraph(GraphDSL.create(resultSink){
    implicit b => sink =>
      import GraphDSL.Implicits._
      val pm3 = b.add(pickMaxOfThree)

      Source(1 to 30) ~> pm3.in(0)
      Source(40 to 80) ~> pm3.in(1)
      Source(25 to 55) ~> pm3.in(2)
      pm3.out ~> sink.in
      ClosedShape
  })

  val result = g.run

  result.onComplete(r => println(r.get))

}
