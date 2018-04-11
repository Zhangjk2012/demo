import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  *
  * Created by ZJK on 2018/2/8.
  */
object TestAsk {
  def main(args: Array[String]): Unit = {


    val system = ActorSystem("TestAsk")
    val actor1 = system.actorOf(Props[Actor1], "actor1")
    val actor2 = system.actorOf(Props(new Actor2(actor1)), "actor2")


  }
}

class Actor1 extends Actor {
  override def receive: Receive = {
    case str: String =>
      println(str)
      println(sender().path)
      Thread.sleep(2000)
      sender() ! "sss====="
  }
}

class Actor2(actor1: ActorRef) extends Actor {

  override def preStart(): Unit = {
    import akka.pattern._
    import scala.concurrent.duration._

    implicit val dispatcher = context.dispatcher

    implicit val timeout = Timeout(1 seconds)

    val future1 = actor1 ask "123456"
//    val future2 = actor1 ask "654321"

    future1.mapTo[String].andThen {
      case Success(v) => println(s"$v   +++++++++++++++")
    }.recover {
      case s: AskTimeoutException => println(s"---==--     ${s.getMessage}")
    }

//    val f = for {
//      f1 <- future1
//      f2 <- future2
//    } yield (f1, f2)
//
//
//
//    f.onComplete {
//      case Success(v) => println(v)
//      case Failure(v) => println(v)
//    }
  }

  override def receive: Receive = {
    case str: String => println(s"$str sfs.234")
  }
}
