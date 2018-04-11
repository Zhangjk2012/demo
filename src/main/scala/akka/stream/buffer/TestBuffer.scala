package akka.stream.buffer

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Attributes, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import scala.concurrent.duration._

/**
  *
  * Created by ZJK on 2017/6/30.
  */
object TestBuffer {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("TestBuffer")

    implicit val materializer = ActorMaterializer()
    // 异步处理消息，也即流的上层传递到下层后，立即可以执行下个消息
//    Source(1 to 3).map(i=> {println(s"A:$i");i}).async
//                  .map(i=> {println(s"B:$i");i}).async
//                  .map(i=> {println(s"C:$i");i}).async
//                  .runWith(Sink.ignore)

    val ss = new TestBuffer().testTick()

    ss.run()

  }

}

case class Tick()

class TestBuffer {

  def testTick() = {

    RunnableGraph.fromGraph(GraphDSL.create(){
      implicit builder => {
        import GraphDSL.Implicits._

        val zipper = builder.add(ZipWith[Tick, Int, Int]((_, count)=>count).addAttributes(Attributes.inputBuffer(1,1)) .async)
        Source.tick(3.second, 3.second, Tick()) ~> zipper.in0

        Source.tick(1.second, 1.second,"message!").conflateWithSeed((x)=>{println("seed:"+x);1})((count,_)=>{ println(s"y:$count");count+1}) ~> zipper.in1
        zipper.out ~> Sink.foreach(println)
        ClosedShape
      }
    })

  }

}
