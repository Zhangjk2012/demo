package fp.chapter1

import scala.annotation.tailrec

//
//class Cafe {
//
//  type CreditCard
//  type Coffee
//
//  def buyCoffee(cc: CreditCard): Coffee = {
//    val coffee = new Coffee
//    coffee
//  }
//
//}

class T {
  var b: Short = _
}

object T extends App {
  val t = new T

  @tailrec
  def sum(s:Int, list: List[Int]): Int = list match {
    case Nil => s
    case h :: tail => sum(s +h, tail)
  }

  val s = sum(0, List(1,2,3,4,5))

  println(s)

}