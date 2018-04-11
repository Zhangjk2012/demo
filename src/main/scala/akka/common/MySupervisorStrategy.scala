package akka.common

import akka.actor.SupervisorStrategy.{Decider, Restart, Stop}
import akka.actor.{ActorInitializationException, ActorKilledException, DeathPactException, OneForOneStrategy, SupervisorStrategy, SupervisorStrategyConfigurator}
import org.slf4j.LoggerFactory

/**
  * Created by zhangjiangke on 2017/6/16.
  */
class MySupervisorStrategy  extends SupervisorStrategyConfigurator {

  val logger = LoggerFactory.getLogger(classOf[MySupervisorStrategy])

  final val defaultDecider: Decider = {
    case _: ActorInitializationException ⇒ Stop
    case _: ActorKilledException ⇒ Stop
    case _: DeathPactException ⇒ Stop
    case _: Exception ⇒ Restart
  }

  override def create(): SupervisorStrategy = {
    OneForOneStrategy()(defaultDecider)
  }
}
