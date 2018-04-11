package test

import scala.collection.mutable.ArrayBuffer

/**
  *
  * Created by ZJK on 2017/7/10.
  */
object TestTrait {
  def main(args: Array[String]): Unit = {

    val queue = new BaseIntQueue with Doubling with Incrementing

    queue.put(10)

    println(queue.get())

  }
}


abstract class IntQueue {

  def get(): Int

  def put(x: Int)

}

class BaseIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]

  override def get(): Int = {
      buf.remove(0)
  }

  override def put(x: Int): Unit = {
    buf += x
  }
}

trait Doubling extends IntQueue {
  abstract override def put(x: Int): Unit = {super.put(x * 2)}
}

trait Incrementing extends IntQueue {
  abstract override def put(x: Int): Unit = {super.put(x + 1)}
}