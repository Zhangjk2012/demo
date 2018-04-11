package akka.stream.cycle

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Concat, GraphDSL, RunnableGraph, Sink, Source, Zip, ZipWith}

/**
  *
  * Created by ZJK on 2017/6/29.
  */
object TestCycle {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem()

    implicit val materializer = ActorMaterializer()(system)

    val cycle = RunnableGraph.fromGraph(GraphDSL.create(){
      implicit builder => {
        import GraphDSL.Implicits._

        val zip = builder.add(ZipWith((left:Int, right:Int)=> left))
        val bcast = builder.add(Broadcast[Int](2))
        val concat = builder.add(Concat[Int]())
        val start = Source.single(0)
        val source = Source.fromIterator(()=>Iterator.from(1))
        source ~> zip.in0
        zip.out.map{s=>println(s);s} ~> bcast ~> Sink.ignore
        start ~> concat ~> zip.in1
        bcast ~> concat
        ClosedShape
      }
    })
    cycle.run()
  }

}
