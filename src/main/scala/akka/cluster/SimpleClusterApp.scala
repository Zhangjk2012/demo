package akka.cluster

import akka.actor.{ActorSystem, Props}
import akka.cluster.metrics.AdaptiveLoadBalancingPool
import akka.cluster.routing.{ClusterRouterGroup, ClusterRouterGroupSettings, ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.{BalancingPool, ConsistentHashingPool, RoundRobinGroup, RoundRobinPool}
import com.typesafe.config.ConfigFactory

object SimpleClusterApp {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseString("akka.cluster.roles = [balancePool,test]")
                                .withFallback(ConfigFactory.load("cluster1.conf"))
    val system = ActorSystem("ClusterSystem", config)

    // Create an actor that handles cluster domain events
    val actor = system.actorOf(Props[SimpleClusterListener], name = "clusterListener")
//    val getActor = system.actorOf(Props[GetActor], name="getActor")
//    system.actorOf(ClusterRouterPool(BalancingPool(0), ClusterRouterPoolSettings(5, 5, false, None)).props(Props[ClusterActor]), "balancePoolActor")
//    val balancePoolActor = system.actorOf(ClusterRouterPool(RoundRobinPool(0), ClusterRouterPoolSettings(15, 5, false, useRoles = "test")).props(Props[ClusterActor]), "balancePoolActor")
    val balancePoolActor = system.actorOf(ClusterRouterGroup(RoundRobinGroup(paths = List.empty), ClusterRouterGroupSettings(15, List("/user/clusterActor"), false, useRoles = "test")).props(), "balancePoolActor")
    Thread.sleep(10000)
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
    balancePoolActor ! "------------------------"
//    val balancingPool = system.actorOf(RoundRobinPool(5).props(Props[ClusterActor]),"nativeClusterActor")
//    balancingPool.tell("sdfwersdfwersdfwerf",getActor)
//    println(balancingPool.path)






  }

}

