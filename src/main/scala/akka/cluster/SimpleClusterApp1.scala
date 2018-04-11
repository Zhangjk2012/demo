package akka.cluster

import akka.actor.{ActorSystem, Props}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.{BalancingPool, RoundRobinPool}
import com.typesafe.config.ConfigFactory

object SimpleClusterApp1 {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [test]")
      .withFallback(ConfigFactory.load("cluster2.conf"))
    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props[SimpleClusterListener1], name = "clusterListener")
//    val balancePoolActor = system.actorOf(ClusterRouterPool(BalancingPool(0), ClusterRouterPoolSettings(5, 5, true, Some("test"))).props(Props[ClusterActor]), "balancePoolActor")
    system.actorOf(Props[ClusterActor], name="clusterActor")

  }

}

