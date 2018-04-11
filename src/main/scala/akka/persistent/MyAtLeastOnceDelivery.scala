package akka.persistent

import akka.actor.{Actor, ActorLogging, ActorSelection, ActorSystem, Props}
import akka.persistence.AtLeastOnceDelivery
import akka.persistence.AtLeastOnceDelivery.UnconfirmedWarning

case class Msg(deliveryId: Long, s: String)
case class Confirm(deliveryId: Long)

sealed trait Evt1
case class MsgSent(s: String) extends Evt1
case class MsgConfirmed(deliveryId: Long) extends Evt1

class MyAtLeastOnceDelivery(destination: ActorSelection) extends AtLeastOnceDelivery with ActorLogging{

  override def persistenceId: String = "MyAtLeastOnceDelivery"


  override def postRestart(reason: Throwable): Unit = {

    println("=================重启了！！！ =================")
    super.postRestart(reason)
  }

  override def receiveRecover: Receive = {
    case evt: Evt1 => {
      log.info(s"receiveRecover${evt}")
      updateState(evt)
    }
  }

  override def receiveCommand: Receive = {
    case s: String           => persist(MsgSent(s))(updateState)
    case Confirm(deliveryId) => persist(MsgConfirmed(deliveryId))(updateState)
    case x: UnconfirmedWarning => {
      val ss = x.unconfirmedDeliveries;

      ss.foreach(s=>{
        log.info(s"${s.message} 失败，${s.destination}, ${s.deliveryId}")
        confirmDelivery(s.deliveryId)
      })

    }
  }

  def updateState(evt: Evt1): Unit = evt match {
    case MsgSent(s) =>
      try {
        deliver(destination)(deliveryId => Msg(deliveryId, s))
      } catch {
        case e: AtLeastOnceDelivery.MaxUnconfirmedMessagesExceededException => {
          println("=++++++++++++++=+++++++++++++++++++++=")
        }
      }

    case MsgConfirmed(deliveryId) => confirmDelivery(deliveryId)
  }

}

class MyDestination extends Actor with ActorLogging{
  def receive = {
    case Msg(deliveryId, s) =>
      log.info(s"Receive a msg: $s")
      sender() ! Confirm(deliveryId)
  }
}

object MyAtLeastOnceDelivery {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem()
    val setting = system.settings.SupervisorStrategyClass

    println(setting)

    val actorDestination = system.actorOf(Props[MyDestination],"actorDestination")

    val actorSelection = system.actorSelection("akka://default/user/actorDestination")
    val atLeastOnceDeliveryActor = system.actorOf(Props(classOf[MyAtLeastOnceDelivery],actorSelection))
//
//    atLeastOnceDeliveryActor ! "a"
//    atLeastOnceDeliveryActor ! "b"
//    atLeastOnceDeliveryActor ! "c"
//    atLeastOnceDeliveryActor ! "d"
//    Thread.sleep(20000)
//    println("执行")
//    atLeastOnceDeliveryActor ! "e"


  }


}
