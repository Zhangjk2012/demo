package akka.supervisor

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}

/**
  *
  * Created by ZJK on 2018/2/28.
  */
class SupervisorActor extends Actor{

  import scala.concurrent.duration._

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(10, 1 minute, false) {
    case _: ArithmeticException      ⇒ Resume
    case _: NullPointerException     ⇒ Restart
    case _: IllegalArgumentException ⇒ Stop
    case _: Exception                ⇒ Escalate
  }

  override def receive: Receive = {
    case p: Props => sender() ! context.actorOf(p, "childActor")
  }
}

class Child extends Actor {
  var state = 0
  override def receive: Receive = {
    case ex: Exception => throw ex
    case x: Int        => state = x
    case "get"         => sender() ! state
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("============================")
    super.preRestart(reason, message)
  }
}
