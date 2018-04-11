package akka

import akka.actor.{Actor, Props, Status, Terminated}
import akka.event.Logging
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.collection.mutable

/**
  * Created by zhangjiangke on 2017/6/8.
  */
class AkkademyDb extends Actor{


//  var router = {
//    val routees = Vector.fill(5) {
//      val r = context.actorOf(Props[AkkademyDb])
//      context watch r
//      ActorRefRoutee(r)
//    }
//    Router(RoundRobinRoutingLogic(), routees)
//  }
//
//
//  val map = new mutable.HashMap[String, Object]
//
//  val log = Logging(context.system, this)

  override def preStart(): Unit = {
    println("preStart")
    super.preStart()
  }

  override def postStop(): Unit = {
    println("postStop")
    super.postStop()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestart")

    super.postRestart(reason)
  }

  override def receive: Receive = {
    case SetRequest(key, value) => {
//      log.info("received SetRequest - key:{} value:{}", key, value)
//      map.put(key, value)
//      sender() ! Status.Success
    }
    case GetRequest(key) => {
//      log.info("received GetRequest - key: {}", key)
//      val response: Option[Object] = map.get(key)
//
//      response match {
//        case Some(x) => sender() ! x
//        case None => sender() ! Status.Failure(new KeyNotFoundException(key))
//      }
    }
    case Terminated(actor) =>
      println(actor + "   +++++++++++++")
    case o =>
      println("Received unknown message:{}", o)
      val senderRef = sender()
      println(senderRef)
      senderRef ! "========="
      context watch(senderRef)
  }
}
