package akka.cluster

import akka.actor.{Actor, ActorLogging, DeadLetter}
import akka.cluster.ClusterEvent._

/**
  * Created by zhangjiangke on 2017/6/20.
  */
class SimpleActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case MemberUp(member) =>
    //      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}", member.address, previousStatus)
    case _: MemberEvent => // ignore
  }
}

class ClusterActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case x: String =>
      println(s" ${System.currentTimeMillis()} ===${Thread.currentThread().getName}")
      Thread.sleep(5000)
  }

  override def preStart(): Unit = {
      println(s"${context.parent.path} +++++")
    //    println(s"$sender() ======")
    //    println(s"Create the cluster actor ${context.system.settings.config.getList("akka.cluster.roles")}")
    super.preStart()
  }
}

class GetActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case x: String => println(s"Receive a text:$x, the self path :${self.path}")
  }
}
