package akka

import akka.actor.{ActorRef, ActorSystem, BootstrapSetup, Props}
import akka.dispatch.{DefaultSystemMessageQueue, Mailbox}
import akka.dispatch.sysmsg.{EarliestFirstSystemMessageList, LatestFirstSystemMessageList, SystemMessage}
import akka.priva.TestPrivate
import akka.remote.transport.TransportAdaptersExtension
import akka.routing.ConsistentHashingPool
import com.typesafe.config.ConfigFactory

/**
  * Created by zhangjiangke on 2017/6/8.
  */
object TestAkka1 extends App{

  val conf1 = ConfigFactory.load("application1.conf")
  val system1 = ActorSystem("LocalTest", conf1)
//
//  // 使用这个，可以根据Key，分配到不同的Actor中，其中第一个参数是，创建几个routees。
////  system.actorOf(ConsistentHashingPool(nrOfInstances = 10, virtualNodesFactor = 2).props(Props[AkkademyDb]))
//
  val actor = system1.actorOf(Props[AkkademyDb], name="actorName")

//  val t = new TestPrivate("==")


//  ss ! ""
//  println(2 & ~2)

//  def create(): Int = {
//    println("==================")
//    1
//  }
//
//  val test1 = create()
//
//  println(test1)
//  println(test1)
//  println(test1)


//  val ss = implicitly(BootstrapSetup)
//  println(ss)
//  println(BootstrapSetup)

////  system.terminate()
//  actor ! "1"
//  actor ! "2"
//  actor ! "3"
//  actor ! "4"
//  actor ! "5"
//  actor ! "6"

//  val testPrivate = new TestPrivate("123")
//  testPrivate.ptr()
//  println(testPrivate.str)

//  system.stop(actor)
//  println(actor.path)
}

private[akka] class TestPrivate private[akka](val str: String) {
  def ptr(): Unit = {
    println(str)
  }
  lazy val tt = ptr()
}




//abstract class TestActor {
//  scalaRef : InternalActorRef ⇒
//    def start()
//
//}
//
//
//class InternalActorRef extends TestActor{
//  override def start(): Unit = ???
//}
//
//class Test1 extends TestActor {
//
//}