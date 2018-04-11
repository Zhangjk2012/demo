package supervisor

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.supervisor.{Child, SupervisorActor}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  *
  * Created by ZJK on 2018/2/28.
  */
class FaultHandlingDocSpec(_system: ActorSystem) extends TestKit(_system)
    with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll{


  def this() = this(ActorSystem("FaultHandlingDocSpec", ConfigFactory.parseString(
    """
      |akka{
      |loggers = [akka.testkit.TestEventListener]
      |loglevel = "warning"
      |}
    """.stripMargin)))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A supervisor" must {
    "apply the chosen strategy for its child" in {
      val supervisor = system.actorOf(Props[SupervisorActor], "supervisor")
      supervisor ! Props[Child]
      val child = expectMsgType[ActorRef]
      println(child)
      child ! 1234
      child ! "get"
      val state = expectMsg[Int](1234)
      println(s"sss = $state")

      watch(child)
//      child ! new NullPointerException("哈哈哈")
      child ! new IllegalArgumentException("=sdf")
      expectMsgPF(){
        case s => println(s)
      }
    }
  }

}
