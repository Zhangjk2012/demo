package test

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
  *
  * Created by ZJK on 2017/9/19.
  */
object TestList {

  def main(args: Array[String]): Unit = {

    val listBuffer = new ListBuffer[Int]

    for (i<- 0 until 1000000) {
      val random = Random.nextInt(20000)
      listBuffer += random
    }

    val list = listBuffer.toList
    println(System.currentTimeMillis())
    println(list.size)
    val l = list.distinct
    println(l.size)
    println(System.currentTimeMillis())


  }


}
