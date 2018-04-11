package akka.persistent

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, SupervisorStrategy}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.typesafe.config.ConfigFactory

/**
  * Created by zhangjiangke on 2017/6/15.
  */



object MyPersistentActor extends App {

  val config = ConfigFactory.load("persistent1.conf")

  val system = ActorSystem();

  val actor = system.actorOf(Props[TestActor],"testActor")

}


class TestActor extends Actor {

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  val actor = context.actorOf(Props[MyPersistentActor],"testPersistent")
  actor ! "a"
//  actor ! "b"
//////  actor ! new Exception("sssssssssssss")
//  actor ! "c"
  override def receive: Receive = {
    case x => println(s"test actor receive $x")
  }
}




class MyPersistentActor extends PersistentActor with ActorLogging{

  override def persistenceId: String = "my-stable-persistence-id"


  override def receiveRecover: Receive = {
    case SnapshotOffer(metadata, offeredSnapshot) => {
      println("=======")
      println(metadata)
      println(offeredSnapshot)

    }
    case RecoveryCompleted => {
      log.info("RecoveryCompleted")
    }
    case x => log.info(s"receiveRecover $x")
  }

  override def receiveCommand: Receive = {

    case ex: Exception => {
      log.info(s"${ex.getMessage}")
      deleteMessages(lastSequenceNr)
      throw ex
    }

    case c: String => {
      sender() ! c
      persist(s"evt-$c-1")(e => sender() ! e)
//      persistAsync(s"evt-$c-1") { e => sender() ! e }
//      persistAsync(s"evt-$c-2") { e => sender() ! e }
////      deferAsync(s"evt-$c-3") { e => sender() ! e }
//      saveSnapshot(s"evt-$c-1")
    }

  }

}
