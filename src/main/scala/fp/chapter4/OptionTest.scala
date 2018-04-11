package fp.chapter4

sealed trait Option[+A] {
  def map[B](f: A => B): Option[B] = this match {
    case Some(s) => Some(f(s))
    case None => None
  }

  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case Some(s) => f(s)
    case None => None
  }

  def getOrElse[B >: A](default: => B): B = this match {
    case Some(s) => s
    case None => default
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
    case Some(_) => this
    case None => ob
  }

  def filter(f: A => Boolean): Option[A] = this match {
    case Some(s) if f(s) => this
    case _ => None
  }
}

case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]


object OptionTest extends App {
//  val list = List(1,34,4,6,100,6,6,6,24,100)
//
//  val ys = list.sortBy(-_)
//
//  val zs = ys.tail.scanLeft((1, ys.head)){
//    case ((i, x), y) => (if (x == y) i else i + 1, y)
//  }
//
//  val result1 = list.groupBy(identity)
//
//  val m = result1.mapValues(_.size)
//
//  val l = m.toList.sortBy(-_._1)
//  val l1 = l.zipWithIndex
//  val l2 = l1.flatMap(tuple => List.fill(tuple._1._2)((tuple._2 + 1, tuple._1._1)))
//
//
//
//  val result = list.groupBy(identity)
//          .mapValues(_.size)
//          .toList.sortBy(-_._1)
//          .zipWithIndex
//          .flatMap(tuple => List.fill(tuple._1._2)((tuple._2+1, tuple._1._1)))


//  println(result)

  def maybeTwice(b: Boolean, i: => Int) = {
    val j = i
    if (b) j + j else 0
  }

  val x = maybeTwice(true, {println("jo"); 4 + 1})



}
