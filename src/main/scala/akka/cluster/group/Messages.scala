package akka.cluster.group

import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout}
import akka.routing
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope

/**
  *
  * Created by ZJK on 2017/7/24.
  */
final case class StatsJob(text: String)

final case class StatsResult(meanWordLength: Double)

final case class JobFailed(reason: String)

class StatsWorker extends Actor {

  var cache = Map.empty[String, Int]

  override def receive: Receive = {
    case word: String =>
      val length = cache get word match {
        case Some(x) => x
        case None => {
          val x = word.length
          cache += (word -> x)
          x
        }
      }
      sender() ! length
    case _ => println("=========")
  }
}

class StatsService extends Actor {

  val workerRouter = context.actorOf(routing.FromConfig.props(Props[StatsWorker]), name = "workerRouter")

  override def receive: Receive = {
    case StatsJob(text) if text != "" ⇒
      val words = text.split(" ")
      val replyTo = sender() // important to not close over sender()
    // create actor that collects replies from workers
    val aggregator = context.actorOf(Props(
      classOf[StatsAggregator], words.size, replyTo))
      words foreach { word ⇒
        workerRouter.tell(
          ConsistentHashableEnvelope(word, word), aggregator)
      }
  }
}

class StatsAggregator(expectedResults: Int, replyTo: ActorRef) extends Actor {
  var results = IndexedSeq.empty[Int]
  import scala.concurrent.duration._
  context.setReceiveTimeout(3.seconds)

  def receive = {
    case wordCount: Int ⇒
      results = results :+ wordCount
      if (results.size == expectedResults) {
        val meanWordLength = results.sum.toDouble / results.size
        replyTo ! StatsResult(meanWordLength)
        context.stop(self)
      }
    case ReceiveTimeout ⇒
      replyTo ! JobFailed("Service unavailable, try again later")
      context.stop(self)
  }
}










