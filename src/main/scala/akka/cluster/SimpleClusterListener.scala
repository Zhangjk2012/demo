package akka.cluster

import akka.actor.{Actor, ActorLogging, Props, RootActorPath}
import akka.cluster.ClusterEvent._
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.BalancingPool
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope

/**
  * Created by zhangjiangke on 2017/6/20.
  */
class SimpleClusterListener extends Actor with ActorLogging{

  val cluster = Cluster(context.system)

  var isGet = false

  override def preStart(): Unit = {
    cluster.subscribe(self, /*initialStateMode = InitialStateAsEvents,*/ classOf[MemberEvent], classOf[UnreachableMember])
  }

  cluster.registerOnMemberUp(println("Myself has up.================"))

  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
      println(member.roles)
      if (member.hasRole("balancePool")) {
//        val path = RootActorPath(member.address) / "user" / "balancePoolActor"
//        println("The path is :" + path)
//        val clusterActor = context.actorSelection(path)
//        println("Send the msg!")
//        val getActor = context.actorOf(Props[GetActor], name="getActor")
//        clusterActor tell ("333333333333333333", getActor)
      }
    case state: CurrentClusterState =>
      log.info("Current members: {}", state.members.mkString(", "))
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
      cluster.down(member.address)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",member.address, previousStatus)
    case _: MemberEvent => // ignore
  }
}

class SimpleClusterListener1 extends Actor with ActorLogging{

  val cluster = Cluster(context.system)

  var isGet = false

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent], classOf[UnreachableMember])
  }


  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
      println(member.roles)
//      if (member.hasRole("balancePool")) {
//          new Thread(()=>{
//            println("Sleep 3 seconds!")
//            Thread.sleep(20000)
//            val path = RootActorPath(member.address) / "user" / "balancePoolActor"
//            println("The path is :" + path)
//            val clusterActor = context.actorSelection(path)
//            val getActor = context.actorOf(Props[GetActor], name=s"getActor${member.address.port.get}")
//
//
//            println("Send the msg!")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg","1")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg1","2")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg2","3")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg3","4")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg4","5")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg5","6")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg6","7")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg7","8")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg8","9")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg9","10")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg10","11")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg11","12")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg12","13")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg13","14")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg14","15")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg15","16")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg16","17")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg17","18")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg18","19")
////            clusterActor ! ConsistentHashableEnvelope("TestMsg20","20")
//            clusterActor tell ("23", getActor)
//            clusterActor tell ("wqr", getActor)
//            clusterActor tell ("vss", getActor)
//            clusterActor tell ("ddd", getActor)
//            clusterActor tell ("sdf", getActor)
//            clusterActor tell ("xcb", getActor)
//            clusterActor tell ("xzxc", getActor)
//            clusterActor tell ("zxvc", getActor)
//            clusterActor tell ("rt", getActor)
//            clusterActor tell ("dd", getActor)
//            clusterActor tell ("ssvz", getActor)
//            clusterActor tell ("adf", getActor)
//            clusterActor tell ("wae22222111", getActor)
//            clusterActor tell ("xxcva", getActor)
//            clusterActor tell ("sdfaeewfq", getActor)
//            clusterActor tell ("xvererg", getActor)
//            clusterActor tell ("asdvgdfvrhgwrgw", getActor)
//            clusterActor tell ("333333333333333333", getActor)
//            clusterActor tell ("11111111111111111111", getActor)
//            clusterActor tell ("123", getActor)
//            println("++++++++++++++++++++++++++++++++++++++++++++++")
//          }).start()
//
//      }

    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
//      cluster.down(member.address)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",member.address, previousStatus)
    case _: MemberEvent => // ignore
  }
}
