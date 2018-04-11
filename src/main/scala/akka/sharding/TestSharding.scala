package akka.sharding

import akka.actor.{ActorLogging, ActorSystem, Props, ReceiveTimeout}
import akka.cluster.Cluster
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardRegion}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

/**
  * Created by zhangjiangke on 2017/6/22.
  */
object TestSharding {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load("cluster.conf")

    val system = ActorSystem("ShardingSystem", config)

    val address = Cluster(system).selfAddress
    Cluster(system).join(address)


    val extractEntityId: ShardRegion.ExtractEntityId = {
      case EntityEnvelope(id, payload) ⇒ (id.toString, payload)
      case msg @ Get(id)               ⇒ (id.toString, msg)
    }

    val numberOfShards = 100

    val extractShardId: ShardRegion.ExtractShardId = {
      case EntityEnvelope(id, _) ⇒ (id % numberOfShards).toString
      case Get(id)               ⇒ (id % numberOfShards).toString
      case ShardRegion.StartEntity(id) ⇒
        (id.toLong % numberOfShards).toString
    }

    ClusterSharding(system).start(
      typeName = "Counter",
      entityProps = Props[Counter],
      settings = ClusterShardingSettings(system),
      extractEntityId = extractEntityId,
      extractShardId = extractShardId)



    val system1 = ActorSystem("ShardingSystem", config)
    Cluster(system1).join(address)
    ClusterSharding(system1).start(
      typeName = "Counter",
      entityProps = Props[Counter],
      settings = ClusterShardingSettings(system),
      extractEntityId = extractEntityId,
      extractShardId = extractShardId)
    val counterRegion = ClusterSharding(system1).shardRegion("Counter")
    counterRegion ! EntityEnvelope(123, Increment)
    counterRegion ! Get(123)
  }

}


case object Increment
case object Decrement
final case class Get(counterId: Long)
final case class EntityEnvelope(id: Long, payload: Any)

case object Stop
final case class CounterChanged(delta: Int)


class Counter extends PersistentActor with ActorLogging{

  import akka.cluster.sharding.ShardRegion.Passivate

  context.setReceiveTimeout(120 seconds)

  var count = 0

  def updateState(event: CounterChanged) {
    log.info("[updateState] updateState")
    count += event.delta}

  override def persistenceId: String = "Counter-"+self.path.name

  override def receiveRecover: Receive = {
    case evt: CounterChanged => updateState(evt)
    case rc: RecoveryCompleted => log.info("Recovery completed:{}", rc)
  }

  override def receiveCommand: Receive = {
    case Increment      ⇒ persist(CounterChanged(+1))(updateState)
    case Decrement      ⇒ persist(CounterChanged(-1))(updateState)
    case Get(_)         ⇒ {
      log.info("[receiveCommand] Get!!!!!")
      sender() ! count
    }
    case ReceiveTimeout ⇒ context.parent ! Passivate(stopMessage = Stop)
    case Stop           ⇒ context.stop(self)
  }

}

















