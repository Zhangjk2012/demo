package akka

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.pattern.{Backoff, BackoffSupervisor}

import scala.concurrent.duration._

/**
  *
  * Created by ZJK on 2018/2/24.
  */
object BackoffSupervisorSpec extends App{

  val childProps = Props(classOf[EchoActor])
  val supervisor = BackoffSupervisor.props(
    Backoff.onStop(
      childProps,
      childName = "myEcho",
      minBackoff = 3.seconds,
      maxBackoff = 30.seconds,
      randomFactor = 0.2 // adds 20% "noise" to vary the intervals slightly
    ))
  val system = ActorSystem("Test")
  val actor = system.actorOf(supervisor, name = "echoSupervisor")

  actor ! "hahaha"
//  actor ! PoisonPill
}

class EchoActor extends Actor {


  override def preStart(): Unit = {
    println("===================")
  }


  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("++++++++++++++++++++++++")
  }


  override def postRestart(reason: Throwable): Unit = println("+++++------++++++")

  override def postStop(): Unit = {
    println("----------")
  }

  override def receive: Receive = {
    case str: String =>
      println(str)
      context stop self
  }
}
