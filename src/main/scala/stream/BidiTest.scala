package stream

import java.nio.ByteOrder

import akka.NotUsed
import akka.stream.BidiShape
import akka.stream.scaladsl.{BidiFlow, Flow, GraphDSL}
import akka.util.ByteString

/**
  * 双向流测试
  * Created by ZJK on 2017/11/8.
  */
object BidiTest extends App {





  def toBytes(msg: Message): ByteString = {
    implicit val order = ByteOrder.LITTLE_ENDIAN
    msg match {
      case Ping(id) => ByteString.newBuilder.putByte(1).putInt(id).result()
      case Pong(id) => ByteString.newBuilder.putByte(2).putInt(id).result()
    }
  }

  def fromBytes(bytes: ByteString): Message = {
    implicit val order = ByteOrder.LITTLE_ENDIAN
    val it = bytes.iterator
    it.getByte match {
      case 1 => Ping(it.getInt)
      case 2 => Pong(it.getInt)
    }
  }

  val codecVerbose = BidiFlow.fromGraph(GraphDSL.create(){
    b =>
      val outbound = b.add(Flow[Message].map(toBytes))
      val inbound = b.add(Flow[ByteString].map(fromBytes))
      BidiShape.fromFlows(outbound, inbound)
  })

  val codec: BidiFlow[Message, ByteString, ByteString, Message, NotUsed] = BidiFlow.fromFunctions(toBytes _, fromBytes _ )

}


trait Message

case class Ping(id: Int) extends Message
case class Pong(id: Int) extends Message

