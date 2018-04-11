package akka.persistent

import akka.actor.{ActorSystem, Props}
import akka.persistence.fsm.PersistentFSM
import akka.persistence.fsm.PersistentFSM.FSMState

import scala.concurrent.duration._
import scala.reflect._
/**
  * Created by zhangjiangke on 2017/6/19.
  */
class MyPersistentFSM extends PersistentFSM[UserState,ShoppingCart ,DomainEvent]{
  override def persistenceId: String = "MyPersistentFSM"

  override def domainEventClassTag: ClassTag[DomainEvent] = classTag[DomainEvent]

  override def applyEvent(event: DomainEvent, cartBeforeEvent: ShoppingCart): ShoppingCart = {
    event match {
      case ItemAdded(item) ⇒ {
        println("1=========")
        cartBeforeEvent.addItem(item)
      }
      case OrderExecuted   ⇒ cartBeforeEvent
      case OrderDiscarded  ⇒ cartBeforeEvent.empty()
    }
  }

  startWith(LookingAround, EmptyShoppingCart)

  when(LookingAround) {
    case Event(AddItem(item), _) ⇒
      println("2=========")
      goto(Shopping) applying ItemAdded(item) forMax (1 seconds)
    case Event(GetCurrentCart, data) ⇒
      stay replying data
  }

  when(Shopping) {
    case Event(AddItem(item), _) ⇒
      println("3=========")
      stay applying ItemAdded(item) forMax (1 seconds)
    case Event(Buy, _) ⇒
      goto(Paid) applying OrderExecuted andThen {
        case NonEmptyShoppingCart(items) ⇒
          println(s"======${items}")
          saveStateSnapshot()
        case EmptyShoppingCart ⇒ saveStateSnapshot()
      }
    case Event(Leave, _) ⇒
      stop applying OrderDiscarded andThen {
        case _ ⇒
          println("ShoppingCardDiscarded ======1")
          saveStateSnapshot()
      }
    case Event(GetCurrentCart, data) ⇒
      stay replying data
    case Event(StateTimeout, _) ⇒
      goto(Inactive) forMax (2 seconds)
  }

  when(Inactive) {
    case Event(AddItem(item), _) ⇒
      goto(Shopping) applying ItemAdded(item) forMax (1 seconds)
    case Event(StateTimeout, _) ⇒
      stop applying OrderDiscarded andThen {
        case _ ⇒ println("ShoppingCardDiscarded ========")
      }
  }

  when(Paid) {
    case Event(Leave, _) ⇒ stop()
    case Event(GetCurrentCart, data) ⇒
      println("GetCurrentCart========")
      stay replying data
  }

}

case class Item(id: String, name: String, price: Float)

sealed trait Command
case class AddItem(item: Item) extends Command
case object Buy extends Command
case object Leave extends Command
case object GetCurrentCart extends Command

//EVENT
sealed trait DomainEvent
case class ItemAdded(item: Item) extends DomainEvent
case object OrderExecuted extends DomainEvent
case object OrderDiscarded extends DomainEvent


//STATE
sealed trait UserState extends FSMState
case object LookingAround extends UserState {
  override def identifier: String = "Looking Around"
}
case object Shopping extends UserState {
  override def identifier: String = "Shopping"
}
case object Inactive extends UserState {
  override def identifier: String = "Inactive"
}
case object Paid extends UserState {
  override def identifier: String = "Paid"
}

//DATA
sealed trait ShoppingCart {
  def addItem(item: Item): ShoppingCart
  def empty(): ShoppingCart
}
case object EmptyShoppingCart extends ShoppingCart {
  def addItem(item: Item) = NonEmptyShoppingCart(item :: Nil)
  def empty() = this
}
case class NonEmptyShoppingCart(items: Seq[Item]) extends ShoppingCart {
  def addItem(item: Item) = NonEmptyShoppingCart(items :+ item)
  def empty() = EmptyShoppingCart
}

object MyPersistentFSM {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("MyPersistentFSM")

    val actor = system.actorOf(Props[MyPersistentFSM], "persistentActor")

    actor ! GetCurrentCart

//    actor ! AddItem(Item("1", "Zhang1", 13.5f))
//    actor ! AddItem(Item("2", "Zhang2", 14.5f))
//    actor ! AddItem(Item("3", "Zhang3", 15.5f))
//    actor ! AddItem(Item("4", "Zhang4", 16.5f))
//    actor ! AddItem(Item("5", "Zhang5", 17.5f))
//
//    actor ! Buy
  }

}

