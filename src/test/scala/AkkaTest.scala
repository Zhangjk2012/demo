import akka.actor.{Actor, ActorKilledException, ActorSystem, Kill, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  *
  * Created by ZJK on 2018/1/22.
  */
class AkkaTest extends TestKit({
  val system = ActorSystem("testsystem", ConfigFactory.parseString("""
      akka.loggers = ["akka.testkit.TestEventListener"]
    """))
  system
}) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll{
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "An Echo Actor" must {
    "send back messages unchanged" in {
      val echo = system.actorOf(Props[EchoActor])
      echo ! "hello world"
      expectMsg("hello world")
    }
  }

  "Event log filter" should {
    "Killed exception" in {
      try {
        val actor = system.actorOf(Props.empty)
        EventFilter[ActorKilledException](occurrences = 1) intercept {
          actor ! Kill
        }
      } finally {

      }
    }
  }
}

class EchoActor extends Actor {
  override def receive: Receive = {
    case msg: String => {
      sender() ! msg
    }
  }
}
