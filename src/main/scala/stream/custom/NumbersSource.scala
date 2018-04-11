package stream.custom

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import scala.concurrent.Future
import scala.reflect.ClassTag

/**
  *
  * Created by ZJK on 2017/11/11.
  */
object NumbersSource extends App {

//  implicit val system = ActorSystem("TestCustom")
//  implicit val m = ActorMaterializer()
//
//  import system.dispatcher
//
//  val sourceGraph: Graph[SourceShape[Int], NotUsed] = new NumbersSource
//
//  val mySource: Source[Int, NotUsed] = Source.fromGraph(sourceGraph)
//
//  val mySink = Sink.fromGraph(new StdoutSink)
//
//  val result: Future[Int] = mySource.take(10).runFold(0)(_ + _)
//
//  val result2: Future[Int] = mySource.take(100).runFold(0)(_ + _)
//
//  mySource.to(mySink).run()


  val names = List("Peter", "Paul", "Mary")

  def ulcase(s: String) = Set(s.toUpperCase(), s.toLowerCase())

  val s = names.flatMap(ulcase)

  println(s)

//  result.onComplete(println)
//  result2.onComplete(println)

}

class StdoutSink extends GraphStage[SinkShape[Int]] {

  val in: Inlet[Int] = Inlet("StdoutSink")

  override def shape = SinkShape(in)

  override def createLogic(inheritedAttributes: Attributes) = new GraphStageLogic(shape) {
    // 从给的输入中拉取消息
    override def preStart(): Unit = pull(in)

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        println(grab(in))
        pull(in)
      }
    })

  }
}

class NumbersSource extends GraphStage[SourceShape[Int]] {

  val out: Outlet[Int] = Outlet("NumbersSource")

  override def shape = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes) = new GraphStageLogic(shape) {
    private var counter = 1
    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, counter)
        counter += 1
      }
    })
  }
}

