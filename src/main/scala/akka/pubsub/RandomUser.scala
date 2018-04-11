package akka.pubsub

import akka.actor.{Actor, ActorLogging, ActorSystem, Address, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberEvent, MemberRemoved, MemberUp}
import akka.cluster.protobuf.msg.ClusterMessages.MemberStatus
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck}
import akka.dispatch.forkjoin.ThreadLocalRandom
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._


/**
  *  Akka cluster的 pub sub功能，当有group时，pub的时候，要写sendEachGroup = true,否则，不会发送。
  *  如果每个sub的group都相同，则只有一个sub会接收消息，如果每个sub的group不一样，则跟不加group效果一样。
  *  Send，使用send时，需要知道接收方的 path，还有，还需要接收方发送Put消息，注册。
  */
object RandomUser {
  case object Tick
  val phrases = Vector(
    "Creativity is allowing yourself to make mistakes. Art is knowing which ones to keep.",
    "The best way to compile inaccurate information that no one wants is to make it up.",
    "Decisions are made by people who have time, not people who have talent.",
    "Frankly, I'm suspicious of anyone who has a strong opinion on a complicated issue.",
    "Nothing inspires forgiveness quite like revenge.",
    "Free will is an illusion. People always choose the perceived path of greatest pleasure.",
    "The best things in life are silly.",
    "Remind people that profit is the difference between revenue and expense. This makes you look smart.",
    "Engineers like to solve problems. If there are no problems handily available, they will create their own problems.")
}

class RandomUser extends Actor{
  import context.dispatcher
  import RandomUser._

  def scheduler = context.system.scheduler
  def rnd = ThreadLocalRandom.current

  val client = context.actorOf(ChatClient.props(self.path.name), "client")


  override def preStart(): Unit = {
    scheduler.scheduleOnce(5.seconds, self, Tick)
  }


  override def postRestart(reason: Throwable): Unit = ()

  override def receive: Receive = {
    case Tick =>
      scheduler.scheduleOnce(rnd.nextInt(5, 20).seconds, self, Tick)
      val msg = phrases(rnd.nextInt(phrases.size))
      println("zhixing!!!!!!!!!!!")
      client ! ChatClient.Publish(msg)
  }
}


object ChatClient {
  def props(name: String): Props = Props(classOf[ChatClient], name)

  case class Publish(msg: String)
  case class Message(from: String, text: String)
}

class ChatClient(name: String) extends Actor {
  val mediator = DistributedPubSub(context.system).mediator
  val topic = "chatroom"
  val sub = new Subscribe(topic, "group1", self)
  mediator ! sub
  println(s"$name joined chat room")

  def receive = {
    case ChatClient.Publish(msg) =>
      val publish = new Publish(topic, ChatClient.Message(name, msg), true)
      mediator ! publish

    case ChatClient.Message(from, text) =>
      val direction = if (sender == self) ">>>>" else s"<< $from:"
      println(s"$name $direction $text")
    case SubscribeAck(Subscribe(xx, Some(x), `self`)) ⇒
      println("sss==="+x)
  }

}

class MemberListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  override def preStart(): Unit =
    cluster.subscribe(self, classOf[MemberEvent])

  override def postStop(): Unit =
    cluster unsubscribe self

  var nodes = Set.empty[Address]

  def receive = {
    case state: CurrentClusterState =>
      nodes = state.members.collect {
        case m if m.status == MemberStatus.Up => m.address
      }
    case MemberUp(member) =>
      nodes += member.address
      log.info("Member is Up: {}. {} nodes in cluster",
        member.address, nodes.size)
    case MemberRemoved(member, _) =>
      nodes -= member.address
      log.info("Member is Removed: {}. {} nodes cluster",
        member.address, nodes.size)
    case _: MemberEvent => // ignore
  }

}


class Destination extends Actor with ActorLogging {
  import akka.cluster.pubsub.DistributedPubSubMediator.Put
  val mediator = DistributedPubSub(context.system).mediator
  // register to the path
  mediator ! Put(self)

  def receive = {
    case s: String ⇒
      log.info("Got {}", s)
  }
}

class Sender extends Actor {
  import akka.cluster.pubsub.DistributedPubSubMediator.Send
  // activate the extension
  val mediator = DistributedPubSub(context.system).mediator

  def receive = {
    case in: String ⇒
      val out = in.toUpperCase
      mediator ! Send(path = "/user/destination", msg = out, localAffinity = true)
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val systemName = "ChatApp"
    val config = ConfigFactory.load("cluster.conf")
    val system1 = ActorSystem(systemName, config)
    val joinAddress = Cluster(system1).selfAddress

    Cluster(system1).join(joinAddress)
    system1.actorOf(Props[MemberListener], "memberListener")
//    system1.actorOf(Props[RandomUser], "Ben")
//    system1.actorOf(Props[RandomUser], "Kathy")

    val destination1 = system1.actorOf(Props[Destination],"destination")



    Thread.sleep(5000)
    val system2 = ActorSystem(systemName, config)
    Cluster(system2).join(joinAddress)
//    system2.actorOf(Props[RandomUser], "Skye")
//

    val destination2 = system2.actorOf(Props[Destination],"destination")
    val sender = system2.actorOf(Props[Sender], "sender")

    sender ! "xxxxxx"


//    Thread.sleep(10000)
//    val system3 = ActorSystem(systemName, config)
//    Cluster(system3).join(joinAddress)
//    system3.actorOf(Props[RandomUser], "Miguel")
//    system3.actorOf(Props[RandomUser], "Tyler")
  }
}
