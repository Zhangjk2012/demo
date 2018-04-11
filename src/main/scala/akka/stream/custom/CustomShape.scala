package akka.stream.custom

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.FanInShape.{Init, Name}
import akka.stream._
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge, MergePreferred, RunnableGraph, Sink, Source}

import scala.collection.immutable

/**
  *
  * Created by ZJK on 2017/6/27.
  */
class CustomShape {

}

// 工作池优先级：有两个输入流，一个输入到MergePreferred中的in中，另一个指定到Preferred中，这样，就优先输出preferred，
// 然后，再把MergePreferred输入到balance，它有多个输出端口，然后输出端口再指定到Merge的输入端口中，最后，由merge的out输出结果。
object CustomShape {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem()

    val materializer = ActorMaterializer()(system)

    val worker1 = Flow[String].map("step 1 " + _)
    val worker2 = Flow[String].map("step 2 " + _)

    RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val priorityPool1 = b.add(PriorityWorkerPool(worker1, 4))
      val priorityPool2 = b.add(PriorityWorkerPool(worker2, 2))

      Source(1 to 100).map("job: " + _) ~> priorityPool1.jobsIn
      Source(1 to 100).map("priority job: " + _) ~> priorityPool1.priorityJobsIn

      priorityPool1.resultsOut ~> priorityPool2.jobsIn
      Source(1 to 100).map("one-step, priority " + _) ~> priorityPool2.priorityJobsIn

      priorityPool2.resultsOut ~> Sink.foreach(println)
      ClosedShape
    }).run()(materializer)


  }

}

//优先级工作池
case class PriorityWorkerPoolShape[In, Out](jobsIn: Inlet[In], priorityJobsIn: Inlet[In], resultsOut: Outlet[Out]) extends Shape {

  override def inlets: immutable.Seq[Inlet[_]] = {

    priorityJobsIn :: jobsIn :: Nil

  }

  override def outlets: immutable.Seq[Outlet[_]] = {

    resultsOut :: Nil

  }

  override def deepCopy(): Shape = {
    PriorityWorkerPoolShape(jobsIn.carbonCopy(), priorityJobsIn.carbonCopy(), resultsOut.carbonCopy())
  }
}

object PriorityWorkerPool {

  def apply[In, Out](worker: Flow[In, Out, Any], workerCount: Int): Graph[PriorityWorkerPoolShape[In, Out], NotUsed] = {
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val priorityMerge = builder.add(MergePreferred[In](1))
      val balance = builder.add(Balance[In](workerCount))
      val resultsMerge = builder.add(Merge[Out](workerCount))
      priorityMerge ~> balance
      for (i <- 0 until workerCount) {
        balance.out(i) ~> worker ~> resultsMerge.in(i)
      }
      PriorityWorkerPoolShape(priorityMerge.in(0), priorityMerge.preferred, resultsMerge.out)
    }
  }
  
}


class PriorityWorkerPoolShape2[In, Out](_init: Init[Out] = Name("PriorityWorkerPool")) extends FanInShape[Out](_init) {

  override protected def construct(init: Init[Out]): FanInShape[Out] = new PriorityWorkerPoolShape2(init)

  val jobsIn = newInlet[In]("jobsIn")
  val priorityJobsIn = newInlet[In]("priorityJobsIn")
}