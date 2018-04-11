package fp.chapter3

import scala.annotation.tailrec

// functional programming in scala. chapter 3.

sealed trait List[+A] // 声明协变的A。

case object Nil extends List[Nothing]

case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List extends App {

  /**
    * 求和
    * @param ints
    * @return
    */
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(h, t) => h + sum(t)
  }

  /**
    * 尾递归实现
    * @param ints
    * @param total
    * @return
    */
  @tailrec
  def sum(ints: List[Int], total: Int): Int = ints match {
    case Nil => total
    case Cons(h, t) => sum(t, h + total)
  }

  /**
    * 乘积
    * @param ds
    * @return
    */
  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0, _) => 0
    case Cons(h, t) => h * product(t)
  }

  /**
    * 乘积的尾递归实现
    * @param ds
    * @param p
    * @return
    */
  def product(ds: List[Double], p: Double): Double = ds match {
    case Nil => p
    case Cons(0, _) => 0
    case Cons(h, t) => product(t, h * p)
  }

  def tail[A](x: List[A]): List[A] = x match {
    case Nil => throw new NoSuchMethodError("Not support this method.")
    case Cons(_, t) => t
  }

  def setHead[A](n: A, x: List[A]): List[A] = x match {
    case Nil => throw new NoSuchMethodError("Not support this method.")
    case Cons(_, t) => Cons(n, t)
  }

  def drop[A](l: List[A], n: Int): List[A] = {
    if (n > 0) {
      l match  {
        case Nil => throw new NoSuchMethodError("Not support this method.")
        case Cons(_, t) => drop(t, n -1)
      }
    } else l
  }

  /**
    * 这个实现，会改变原有的顺序
    * @param l
    * @param f
    * @tparam A
    * @return
    */
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = {
    def go(l: List[A], nList: List[A]): List[A] = l match {
      case Nil => nList
      case Cons(h, t) => {
        if (!f(h)) {
          val tmp = Cons(h, nList)
          go(t, tmp)
        } else {
          go(t, nList)
        }
      }
    }
    go(l, Nil)
  }

  def dropWhile1[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(h, t) => {
      if (f(h)) {
        dropWhile1(t, f)
      } else Cons(h, dropWhile1(t, f))
    }
  }

  def dropWhile2[A](l: List[A], f: A => Boolean): List[A] =
    l match {
      case Cons(h,t) if f(h) => dropWhile2(t, f)
      case _ => l
    }

  def init[A](l: List[A]): List[A] = l match {
    case Nil => throw new NoSuchMethodError("====")
    case Cons(_, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))
  }

  /**
    * 创建一个List
    * @param as
    * @tparam A
    * @return
    */
  def apply[A](as: A*): List[A] = {
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
  }

  val x = List(1,2,3,4,5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def foldRight[A,B](as: List[A], z: B)(f:(A,B) => B): B = {
    as match {
      case Nil => z
      case Cons(h, t) => f(h, foldRight(t, z)(f))
    }
  }

  def sum2(ns: List[Int]) = foldRight(ns, 0)((x, y)=> x + y)

  def length[A](ns: List[A]) = foldRight(ns, 0)((_, y) => y + 1)


  def foldLeft[A,B](ns: List[A], z: B)(f: (A,B) => B): B = ns match {
    case Nil => z
    case Cons(h, t) => foldLeft(t, f(h,z))(f)
  }

  def sum3(ns: List[Int]) = foldLeft(ns, 0)(_ + _)

  def product3(ns: List[Int]) = foldLeft(ns, 1)(_ * _)

  def length2[A](ns: List[A]): Int = foldLeft(ns, 0)((_,y)=> y + 1)

  def reverse[A](ns: List[A]): List[A] = {
    Nil
  }

  //反转字符串，使用foldLeft
  def reverseWithFold[A](ns: List[A]): List[A] = foldLeft(ns, List[A]())((x,y) => Cons(x, y))

  def foldRightViaFoldLeft[A, B](ns: List[A], z: B)(f: (A,B)=>B): B = foldLeft(reverseWithFold(ns), z)(f)

  def add1(ns: List[Int]): List[Int] = foldRightViaFoldLeft(ns, Nil:List[Int])((x, y) => Cons(x + 1, y))

  println(add1(Cons(1, Cons(2, Cons(3, Nil)))))
}
