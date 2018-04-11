package akka

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by zhangjiangke on 2017/6/8.
  */
object TestAkkaClient {

  def main(args: Array[String]): Unit = {


    implicit val timeout = Timeout(2 seconds)

    val conf = ConfigFactory.load("application2.conf")

    val system = ActorSystem("LocalSystem", conf)

    import scala.concurrent.ExecutionContext.Implicits.global

    val remoteDb = system.actorSelection("akka.tcp://LocalTest@127.0.0.1:2552/user/local")

    remoteDb ? SetRequest("123","1234")

    val future = remoteDb ? GetRequest("1234")

    future.onComplete({
      case Failure(e) if (e.isInstanceOf[KeyNotFoundException]) => {
        val f = e.asInstanceOf[KeyNotFoundException]
        println(f.key)
      }
      case Success(s) => println(s)
    })
  }

}
