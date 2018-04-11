package stream

import java.nio.file.Paths

import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, IOResult, ThrottleMode, javadsl}
import akka.{Done, NotUsed}
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

/**
  *
  * Created by ZJK on 2017/11/7.
  */
object Main extends App {

  implicit val system = ActorSystem("Stream-Test")

  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 1000000000)

//  val done: Future[Done] = source.runForeach(println)(materializer)

  val factorials = source.scan(BigInt(1))((acc, next)=> acc * next)
  import scala.concurrent.duration._
  factorials.zipWith(Source(0 to 1000000000))((num, idx) => s"$idx! = $num")
          .throttle(1, 1 seconds, 1, ThrottleMode.shaping).runForeach(println)

  Source.maybe[Int]






//  val result: Future[IOResult] = factorials.map(_.toString()).runWith(lineSink("factorials.txt"))
//
//  implicit val ec = system.dispatcher
//
//  result.onComplete(r => {
//    val iOResult = r.get
//    println(iOResult.count);
//    println(iOResult.status.get);
//    system.terminate()
//  })

//  done.onComplete(_=> system.terminate)



  def lineSink(filename: String): Sink[String, Future[IOResult]] = {
    val mm: Flow[String, ByteString, NotUsed] = Flow[String].map(s => ByteString(s + "\n"))
    mm.toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
  }



}
