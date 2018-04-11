package akka.stream.source

import akka.actor.{Actor, ActorSystem, Props}
import akka.stream._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, Sink, Source, Zip}

import scala.concurrent.duration._

/**
  *
  * Created by ZJK on 2017/6/27.
  */
object TestSource {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("SourceGraph")

    implicit val m = ActorMaterializer()(system)

//    testSourceShape
//    testFlowShape
//    testMerge
    testRemote


  }



  def testSourceShape(implicit m: ActorMaterializer): Unit = {
    val pairs = Source.fromGraph(GraphDSL.create(){
      implicit builder => {
        import GraphDSL.Implicits._
        val zip = builder.add(Zip[Int, Int]())
        def ints = Source.fromIterator(()=>Iterator.from(1))
        ints.filter(_ % 2 != 0) ~> zip.in0
        ints.filter(_ % 2 == 0) ~> zip.in1
        SourceShape(zip.out)
      }
    })
    pairs.throttle(1, 1.second, 1, ThrottleMode.shaping).runForeach(println)
  }


  def testFlowShape(implicit m: ActorMaterializer): Unit = {

    val pairUpWithToString = Flow.fromGraph(GraphDSL.create(){
      implicit builder => {

        import GraphDSL.Implicits._

        val broadcast = builder.add(Broadcast[Int](2))
        val zip = builder.add(Zip[Int,String]())

        broadcast.out(0).map(identity) ~> zip.in0
        broadcast.out(1).map(_.toString) ~> zip.in1


        FlowShape(broadcast.in,zip.out)
      }
    })

    pairUpWithToString.runWith(Source.fromIterator(()=>Iterator.from(1)),Sink.foreach(println))

  }


  def testMerge(implicit m: ActorMaterializer): Unit = {

    val source1 = Source.single(1)
    val source2 = Source.single(2)

    val merged = Source.combine(source1, source2)(Merge(_))

    val mergedResult = merged.runWith(Sink.fold(1)(_+_))

    implicit val dispatcher = m.system.dispatcher

    mergedResult.onComplete(x=> {
      println(x.get)
      m.system.terminate()
    })

  }

  def testRemote(implicit m: ActorMaterializer): Unit = {
    val actorRef = m.system.actorOf(Props[TestRemoteActor])
    val sendRemotely = Sink.actorRef(actorRef, "Done")

    val localProcessing = Sink.foreach[Int](println)

    val sink = Sink.combine(sendRemotely, localProcessing)(Broadcast[Int](_))

    Source(List(1,2,3)).runWith(sink)

  }



}

class TestRemoteActor extends Actor {
  override def receive: Receive = {
    case x if x == "Done" => {
      println("Done")
      context.stop(self)
      context.system.terminate()
    }
    case x => println("===="+x)
  }
}
