package metrics

import akka.actor.{Actor, ActorSystem, Props}
import akka.cluster.metrics.AdaptiveLoadBalancingPool
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import com.typesafe.config.ConfigFactory

/**
  *
  * Created by ZJK on 2017/8/25.
  */
object TestMetrics {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load("metrics1.conf")
//    val config2 = ConfigFactory.load("metrics2.conf")

    val system1 = ActorSystem("MetricsSystem",config)
//    val system2 = ActorSystem("MetricsSystem",config2)
    system1.actorOf(Props[MetricsListener])
    val balancePoolActor = system1.actorOf(ClusterRouterPool(AdaptiveLoadBalancingPool(), ClusterRouterPoolSettings(15, 5, true)).props(Props[TestMetrics]), "balancePoolActor")
//    system2.actorOf(Props[MetricsListener])

  }
}

class TestMetrics extends Actor {
  override def receive: Receive = {
    case str:String => println(str)
  }
}
