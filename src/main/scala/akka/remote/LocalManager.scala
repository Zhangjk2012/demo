package akka.remote

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by zhangjiangke on 2017/6/13.
  */
class LocalManager extends Actor{
  override def receive: Receive = {

    case x => println(x)

  }
}

object LocalManager {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load("application1.conf")
    val system = ActorSystem("LocalManager", config)
  }


}
