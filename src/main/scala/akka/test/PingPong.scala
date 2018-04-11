package akka.test

import akka.actor.SupervisorStrategy.{Decider, Escalate, Restart, Resume, Stop, defaultDecider}
import akka.actor.{Actor, ActorInitializationException, ActorKilledException, ActorRef, ActorSystem, DeathPactException, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by zhangjiangke on 2017/6/13.
  */
object PingPong {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("pingpong")


    val pinger = system.actorOf(Props[Pinger], "pinger")
    val ponger = system.actorOf(Props(classOf[Ponger], pinger, 1000), "ponger")




//    import system.dispatcher
//    system.scheduler.scheduleOnce(500 millis) {
//      ponger ! Ping
//    }


//    ponger ! Ping


//    ponger ! Ping

//    val arg = new Argument("123")
//    val actor = system.actorOf(Props(classOf[ValueClassActor], arg.value))
    //actor ! new ArithmeticException

  }

}

case object Pong

case object Ping

class Pinger extends Actor {
  var countDown = 100
  override def receive: Receive = {
    case Pong => {
      println(s"${self.path} received pong, count down $countDown")
      sender() ! "sdfasdfafwefafas"
//      if (countDown > 0) {
//        countDown -= 1
//        sender() ! Ping
//      } else {
//        sender() ! PoisonPill
//        self ! PoisonPill
//      }
    }
    case x: ArithmeticException => {
      throw x
    }
  }

  override def preStart(): Unit = {
    println("=====preStart")
  }

  override def postStop(): Unit = {
    println("postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestart")
    super.postRestart(reason)
  }
}

class Ponger(pinger: ActorRef, count: Int) extends Actor {

  import akka.pattern.ask
  import scala.concurrent.ExecutionContext.Implicits.global
  println(count)
  override def receive: Receive = {
    case str:String ⇒ println("=============================")
    case Ping =>
      println(s"${self.path} received ping")
      // 会阻塞其它消息的处理，所以这里尽量不要用阻塞时间长的操作。
      Thread.sleep(5000)
      implicit val timeout = Timeout(3 second)
      pinger ? Pong onComplete {
        case Success(res) => println("ask success")
        case Failure(exp) => println("ask failure")
      }
  }
}



class Argument(val value: String) extends AnyVal

class ValueClassActor(arg: Argument) extends Actor {

  val defaultDecider: Decider = {
    case _: ActorInitializationException ⇒ Stop
    case _: ActorKilledException         ⇒ Stop
    case _: DeathPactException           ⇒ Stop
    case _: ArithmeticException          => {
      println("===============")
      Resume
    }
    case _: NullPointerException         => Restart
    case _: IllegalArgumentException     => Stop
    case _: Exception                    ⇒  Escalate
  }
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(10, 1 minute)(defaultDecider)

//  val child = context.actorOf(Props[Pinger])
//  child ! new ArithmeticException


  def receive = {
    case x: ArithmeticException => {
      println(x)
      throw x
    }
    case x: NullPointerException => {
      println(x)
      throw x
    }
    case _ => println(arg.value)
  }

  override def preStart(): Unit = {
    println("preStart")
  }

  override def postStop(): Unit = {
    println("postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestart")
    super.postRestart(reason)
  }
}
