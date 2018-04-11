package akka

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.util.Random

//请求
case class QuoteRequest()

//回复
case class QuoteResponse(quoteString: String)

trait TestMethod {
  def test()
}

trait TestMethod1 extends TestMethod{
  abstract override def test() {
    println("=============")
    super.test
  }
}

trait TestMethod2 extends TestMethod with TestMethod1{
  abstract override def test() {
    println("++++++++++++++++++++")
    super.test()
  }
}

class Tt extends TestMethod {
  override def test(): Unit = {
    println("-------------------")
  }
}

class TestMethod3 extends Tt with TestMethod1 with TestMethod2 {
    def test1(): Unit = {
      test()
    }
}


class TeacherActor extends Actor with ActorLogging {

  val quotes = List("Moderation is for cowards",
                    "Anything worth doing is worth overdoing",
                    "The trouble is you think you have time",
                    "You never gonna know if you never even try")

  override def receive = {
    case QuoteRequest =>
      val random = Random.nextInt(4)
      val response = QuoteResponse(quotes(random))
      println(response)
      sender() ! response
  }
}

class StudentActor extends Actor{

  val teacherActorRef = context.actorOf(Props[TeacherActor])
  import scala.concurrent.duration._
  import context.dispatcher
  //3秒后执行往TeacherActor发送QuoteRequest消息
  context.system.scheduler.schedule(1 seconds, 2 seconds, teacherActorRef, QuoteRequest)

  override def receive = {
    case res:QuoteResponse => {
      println(s"Receive the response msg: $res")
    }
  }
}


object TestAkka extends App{

//  val conf2 = ConfigFactory.load("application2.conf")
//  val system2 = ActorSystem("RemoteTest", conf2)
//
//  val studentActorRef = system2.actorOf(Props[StudentActor])

//  val t = new TestMethod3
//  t.test1()


  val hashMap = new java.util.HashMap[String, String]()

  val key = "test"
  hashMap.put(key, "00000")

  val newValue = hashMap.compute(key, (k:String , v: String) =>{
    println(k)
    println(v)
    null
  })

  println(newValue)
  println(hashMap)




  // 使用这个，可以根据Key，分配到不同的Actor中，其中第一个参数是，创建几个routees。
  //  system.actorOf(ConsistentHashingPool(nrOfInstances = 10, virtualNodesFactor = 2).props(Props[AkkademyDb]))

//  val ss = system2.actorSelection("akka.tcp://LocalTest@127.0.0.1:2552/user/actorName")
//  val watch = system2.actorOf(Props[TeacherActor], "testAkka")
//  ss.tell("123", watch)
//
//  Thread.sleep(5000)
//  system2.terminate()



//  import system2.dispatcher
//
//  val addressesPromise: Promise[String] = Promise()
//  println("=================")
//  val future = addressesPromise.future
//  println(future)
//  addressesPromise.success("ssssssss")
//  future.foreach(_ => println("123456789"))
//  future.foreach(_ => println("123456789"))
//  future.foreach(_ => println("123456789"))
//  future.foreach(_ => println("123456789"))
//  val f = future.map(_ => "999999999999999")
//  println(f)
//  println("+++++++++++++++++++")
//  println(future)
//  println(f)
//  println("--------------------")
//  println(future.isCompleted)
//  println(future.value)




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

package priva {
  private[akka] class TestPrivate private[akka](val str: String) {
    def ptr(): Unit = {
      println(str)
    }
  }
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