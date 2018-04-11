package stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

/**
  * 流的状态本来就是顺序执行，假设使用async，则变成异步执行
  * Created by ZJK on 2017/11/10.
  */
object AsyncTest extends App {

  implicit val system = ActorSystem("TestAsync")
  implicit val m = ActorMaterializer()

  Source(1 to 3).map{i=> println(s"A:$i"); i}.async
                .map{i=> println(s"B:$i"); i}.async
                .map{i=> println(s"C:$i"); i}.async
                .runWith(Sink.ignore)

}
