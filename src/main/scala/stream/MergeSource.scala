package stream

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, OverflowStrategy, SourceShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}

import scala.concurrent.Future

/**
  *
  * Created by ZJK on 2017/11/7.
  */
object MergeSource extends App {


  implicit val system = ActorSystem("OtherShape")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher


  val source = RunnableGraph.fromGraph(GraphDSL.create(){
    implicit b =>
      import GraphDSL.Implicits._
      val merge = b.add(Merge[Int](2))
      val bcast = b.add(Broadcast[Int](2))
      Source.single(1) ~> merge ~> bcast ~> Flow[Int].map(i => {println(i); i}) ~> Sink.ignore
      bcast ~> Flow[Int].buffer(10, OverflowStrategy.dropHead) ~> merge
//      Source(1 to 100000) ~> merge
    ClosedShape
  }).run()


//  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
//    import GraphDSL.Implicits._
//
//    val merge = b.add(Merge[Int](2))
//    val bcast = b.add(Broadcast[Int](2))
//    val source = Source(1 to 100)
//    source ~> merge ~> Flow[Int].map { s => println(s); s } ~> bcast ~> Sink.ignore
//    merge <~ Flow[Int].buffer(10, OverflowStrategy.dropHead) <~ bcast
//    ClosedShape
//  })
//
//  val result = g.run();
//
//  println(result)


//  val sourceOne = Source(List(1))
//  val sourceTwo = Source(List(2))
//  val merged = Source.combine(sourceOne, sourceTwo)(Merge(_))
//
//  val mergedResult: Future[Int] = merged.runWith(Sink.fold(0)(_ + _))
//  mergedResult.onComplete(println)

}
