package akka.cluster.singleton

import akka.actor.{Actor, ActorLogging, ActorPath, ActorSystem, Address, PoisonPill, Props, RootActorPath}
import akka.cluster.{Cluster, MemberStatus}
import akka.cluster.ClusterEvent._
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

case class RegisterReportMsg(ip: String)

class RegisterActor extends Actor with ActorLogging{

  val cluster = Cluster(context.system)
  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent], classOf[ReachabilityEvent])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  import context.dispatcher
  val tickTask = context.system.scheduler.scheduleOnce(5.seconds, self, "tick")
  var address: Address = _


  override def receive = {
    case _:String =>
      println("===========        " + _)
      val singletonPath = RootActorPath(address) / "user" / "statsServiceProxy"
      val actorSelection = context.actorSelection(singletonPath)
      actorSelection ! RegisterReportMsg("192.168.1.111")
    case state: CurrentClusterState =>
      state.members.collect{
        case m if m.hasRole("pushpull") && m.status == MemberStatus.Up => log.info(m.toString())
      }
    case MemberUp(m) if m.hasRole("first")        => address = m.address; log.info("Add:" + m.toString())
    case other: MemberEvent                         => log.info("other:" + other.toString())
    case UnreachableMember(m)                       => cluster.down(m.address);log.info("unreachable:" + m.toString())
    case ReachableMember(m) if m.hasRole("pushpull") => log.info("reachable:" + m.toString())
  }
}

class PushPullActor extends PersistentActor with ActorLogging {
  override def persistenceId = "PushPullService"

  override def receiveRecover = {
    case SnapshotOffer(metadata, offeredSnapshot) => {
      println("recover:=======")
      println(metadata)
      println(offeredSnapshot)
    }
    case RecoveryCompleted => {
      log.info("RecoveryCompleted")
    }
    case x => log.info(s"receiveRecover $x")
  }

  override def receiveCommand = {
    case r:RegisterReportMsg => {
      context watch sender
      log.error(s"Receive msg:$r   sender=$sender()")
//      saveSnapshot(r)
    }
  }
}

object Singleton extends App{
  val config = ConfigFactory.load("persistent1.conf")
  val system = ActorSystem("ClusterSystem",config)
  system.actorOf(ClusterSingletonManager.props(
    singletonProps = Props[PushPullActor],
    terminationMessage = PoisonPill,
    settings = ClusterSingletonManagerSettings(system).withRole("pushpull")),
    name = "statsService")

  system.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/statsService",
    settings = ClusterSingletonProxySettings(system).withRole("pushpull")),
    name = "statsServiceProxy")
}

object Singleton1 extends App{

  val config = ConfigFactory.load("persistent2.conf")

  val system = ActorSystem("ClusterSystem",config)

  system.actorOf(ClusterSingletonManager.props(
    singletonProps = Props[PushPullActor],
    terminationMessage = PoisonPill,
    settings = ClusterSingletonManagerSettings(system).withRole("pushpull")),
    name = "statsService")

  system.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/statsService",
    settings = ClusterSingletonProxySettings(system).withRole("pushpull")),
    name = "statsServiceProxy")
}

object Singleton3 extends App{

  val system = ActorSystem("ClusterSystem")

//  val persistentActor = system.actorOf(Props[PushPullActor])


  system.actorOf(Props[RegisterActor])
}