package akka.stream.bidirectional

import java.nio.ByteOrder

import akka.stream.BidiShape
import akka.stream.scaladsl.{BidiFlow, Flow, GraphDSL}
import akka.util.ByteString

/**
  *
  * Created by ZJK on 2017/6/28.
  */
object TestBidirectional {

  def toBytes(msg: Message): ByteString = {
    // 小字节在前
    implicit val order = ByteOrder.LITTLE_ENDIAN
    msg match {
      case Pong(id) => ByteString.newBuilder.putByte(1).putInt(id).result()
      case Ping(id) => ByteString.newBuilder.putByte(2).putInt(id).result()
    }
  }

  def fromBytes(bytes: ByteString): Message = {
    implicit val order = ByteOrder.LITTLE_ENDIAN
    val it = bytes.iterator
    it.getByte match {
      case 1     => Ping(it.getInt)
      case 2     => Pong(it.getInt)
      case other => throw new RuntimeException(s"parse error: expected 1|2 got $other")
    }
  }

  val codecVerbose = BidiFlow.fromGraph(GraphDSL.create(){
    implicit builder => {
      val outbound = builder.add(Flow[Message].map(toBytes))
      val inbound = builder.add(Flow[ByteString].map(fromBytes))
      BidiShape.fromFlows(outbound, inbound)
    }
  })

}



sealed trait Message

case class Ping(id: Int) extends Message
case class Pong(id: Int) extends Message







