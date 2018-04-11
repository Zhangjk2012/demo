package akka

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern.{ask, pipe}

final case class Result(x: Int, s: String, d: Double)
case object Request

class TestActor1 extends Actor {
  override def receive: Receive = {
    case str =>
      println(s"1    $str")
      sender() ! 123.23
  }
}
class TestActor2 extends Actor {
  override def receive: Receive = {
    case str =>
      println(s"2    $str")
      sender() ! "haha"
  }
}
class TestActor3 extends Actor {
  override def receive: Receive = {
    case str =>
      println(s"3    $str")
      sender() ! 123.2d
  }
}
class TestActor4 extends Actor {
  override def receive: Receive = {
    case str => println(s"4    $str")
  }
}

object TestFuture extends App {

  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("TestFuture")

  val actorA = system.actorOf(Props[TestActor1], "actorA")
  val actorB = system.actorOf(Props[TestActor2], "actorB")
  val actorC = system.actorOf(Props[TestActor3], "actorC")
  val actorD = system.actorOf(Props[TestActor4], "actorD")

  import system.dispatcher // The ExecutionContext that will be used
  val f: Future[Result] =
    for {
      x ← ask(actorA, Request).mapTo[Int] // call pattern directly
      s ← (actorB ask Request).mapTo[String] // call by implicit conversion
      d ← (actorC ? Request).mapTo[Double] // call by symbolic name
    } yield Result(x, s, d)

  f pipeTo actorD
  pipe(f) to actorD

}
