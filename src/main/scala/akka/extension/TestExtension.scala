package akka.extension

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.config.ConfigFactory

/**
  * Created by zhangjiangke on 2017/6/22.
  */
class TestExtension1 extends Extension{

  private val counter = new AtomicInteger(0)

  def increment() = counter.incrementAndGet()

}

object TestExtension extends ExtensionId[TestExtension1] with ExtensionIdProvider {
  println("初始化！！！！")

  override def createExtension(system: ExtendedActorSystem): TestExtension1 = new TestExtension1()

  override def lookup(): ExtensionId[TestExtension1] = {
    println("lookup!!!!")
    TestExtension
  }

  override def get(system: ActorSystem): TestExtension1 = super.get(system)
}

object TestMain {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load("extension.conf")

    val system = ActorSystem("ExtensionTest", config)

    println(TestExtension(system).increment())


  }

}