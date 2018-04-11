package fp.chapter5

import scala.collection.mutable.ListBuffer

sealed trait Stream[+A] {
  def toList: List[A] = {
    val listBuffer = new ListBuffer[A]
    def go(stream: Stream[A]): List[A] = stream match {
      case Empty => listBuffer.toList
      case Cons(h, t) => {
        listBuffer += h()
        go(t())
      }
    }
    go(this)
  }

  def take(n: Int): List[A] = {
    val listBuffer = new ListBuffer[A]
    def go(stream: Stream[A], n: Int): List[A] = stream match {
      case Cons(h, t) if n > 0 => {
        listBuffer += h()
        go(t(), n - 1)
      }
      case _ => listBuffer.toList
    }
    go(this, n)
  }

  def drop(n: Int): List[A] = {
    def go(n: Int, stream: Stream[A]): List[A] = stream match {
      case Empty => List.empty
      case Cons(_, t) if n > 0 => go(n - 1, t())
      case s => s.toList
    }
    go(n, this)
  }

  import Stream._

  def takeStream(n: Int): Stream[A] = this match {
    case Empty => Stream.empty
    case Cons(h, t) if n > 1 => Stream.cons(h(), t().takeStream(n - 1))
    case Cons(h, _) if n == 1 => Stream.cons(h(), Stream.empty)
  }

  def dropStream(n: Int): Stream[A] = this match {
    case Empty => empty
    case Cons(_ ,t) if n > 0 => t().dropStream(n - 1)
    case _ => this
  }

  def foldRight[B](a: => B)(f: (A, => B) => B): B = {
    this match {
      case Empty => a
      case Cons(h, t) => f(h(), t().foldRight(a)(f))
    }
  }

  def exists(p: A => Boolean): Boolean = foldRight(false)((x, y) => p(x) || y)

  def forAll(p: A => Boolean): Boolean = foldRight(true)((x, y) => {
    if (p(x)) y else false
  })

  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = {
    foldRight(empty[A])((x, y) => {
      if (p(x)) cons(x, y)
      else empty[A]
    })
  }

  def headOption: Option[A] = {
    foldRight(None: Option[A])((h, _) => Some(h))
  }

}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream extends App {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] = if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))


  val stream = Stream(1,2,3,4,5,6,7,8)

  println(stream.forAll(x => x < 2))

}
