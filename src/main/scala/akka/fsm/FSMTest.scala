package akka.fsm

import akka.actor.{ActorRef, ActorSystem, FSM, Props}

import scala.collection.immutable.Seq
import scala.concurrent.duration._

/**
  * Created by zhangjiangke on 2017/6/14.
  */
object FSMTest {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem()

    val buncher = system.actorOf(Props(classOf[Buncher]))

    buncher ! SetTarget(buncher)
    buncher ! Queue(56)
    buncher ! Queue(561)
    buncher ! Queue(562)
    buncher ! Queue(563)

  }

}


class Buncher extends FSM[State, Data] {

  startWith(Idle, Uninitialized)

  when(Idle) {
    case Event(SetTarget(ref), Uninitialized) =>
      stay using Todo(ref, Vector.empty)
  }

  onTransition {
    case Active -> Idle =>
      stateData match {
      case Todo(ref, queue) => ref ! Batch(queue)
      case _                => // nothing to do
    }
  }

  when(Active, stateTimeout = 1 second) {
    case Event(Flush | StateTimeout, t: Todo) =>
      goto(Idle) using t.copy(queue = Vector.empty)
    case Event(Queue(obj), t @ Todo(_, v)) =>
      log.info("==============================")
      stay using t.copy(queue = v :+ obj)
  }

  whenUnhandled {
    // common code for both states
    case Event(Queue(obj), t @ Todo(_, v)) =>
      log.info("+++++++++++++++++++++++++++++++")
      goto(Active) using t.copy(queue = v :+ obj)

    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      stay
  }

  initialize()
}





final case class SetTarget(ref: ActorRef)
final case class Queue(obj: Any)
case object Flush

// sent events
final case class Batch(obj: Seq[Any])


// states
sealed trait State
case object Idle extends State
case object Active extends State

sealed trait Data
case object Uninitialized extends Data
final case class Todo(target: ActorRef, queue: Seq[Any]) extends Data