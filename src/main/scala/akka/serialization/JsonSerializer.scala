package akka.serialization

import java.nio.charset.Charset

import akka.actor.ExtendedActorSystem
import com.alibaba.fastjson.JSON

/**
  * Created by zhangjiangke on 2017/6/16.
  */
class JsonSerializer(val system: ExtendedActorSystem) extends BaseSerializer {

  override def toBinary(o: AnyRef): Array[Byte] = {
    println(o)
    val b = JSON.toJSONBytes(o)
    println("+++" + b.length)
    b
  }

  override def includeManifest: Boolean = true

  override def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = {
    val obj = JSON.parseObject(bytes, 0, bytes.length, Charset.defaultCharset(), manifest.get)
    println(obj)
    obj
  }
}
