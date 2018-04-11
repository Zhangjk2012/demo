package fp.chapter2

import scala.annotation.tailrec

/**
  *
  * Created by ZJK on 2018/1/10.
  */
object HighOrderFunction extends App {

  def factorial(n: Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int = {
      if (n <=0) acc
      else go(n - 1, acc * n)
    }
    go(n, 1)
  }

  def fib(n: Int): Double = {

    def go(pre: Int, acc: Double, preAcc: Double): Double = {
      if (pre == 1) preAcc
      else go(pre - 1, acc + preAcc, acc)
    }
    go(n, 1, 0)
  }

  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    @tailrec
    def loop(n: Int): Boolean = {
      if (n > as.length - 2) true
      else if (!ordered(as(n), as(n + 1))) false
      else loop(n + 1)

    }
    loop(0)
  }

  /**
    * 接收一个f函数，然后，返回一个接收A的参数返回一个 接收B 生成C的函数
    * 在REPL中，返回的值是curry: [A, B, C](f: (A, B) => C)A => (B => C)
    */
  def curry[A, B, C](f:(A, B)=> C): A => (B => C) = (a: A) => (b: B) => f(a, b)

  /**
    * test: (Int, Int) => String = $$Lambda$1047/1509860853@26d028f7
    */
  val test = (a1: Int, a2: Int) => {(a1 + a2).toString}

  /**
    * c1: Int => (Int => String) = $$Lambda$1052/1117719720@6b030101
    * 这里，是接收一个Int，返回一个  接收Int返回String的函数。
    */
  val c1 = curry(test)

  /**
    * c2: Int => String = $$Lambda$1053/1686632440@2b62475a
    * 到这里，就已经恢复到 平常函数了，即： 接收一个Int，返回一个String
    */
  val c2 = c1(1)

  /**
    * c3: String = 3
    * 返回一个字符串
    */
  val c3 = c2(2)


  /**
    * 这个过程，正好与curry过程相反。也即，接收一个 A 生成 B=>C的函数，然后，要求返回一个接收A，B 参数返回C的函数
    * uncurry: [A, B, C](f: A => (B => C))(A, B) => C
    */
  def uncurry[A,B,C](f:A=>B=>C): (A, B) => C = (a: A, b: B) => f(a)(b)

  /**
    * 这是一个  字符串的 乘积
    * testUncurry: (a: Int)(b: Int)(c: String)String
    */
  def testUncurry(a: Int)(b: Int)(c: String) = c * (a + b)

  /**
    * u1: (Int, Int) => String => String = $$Lambda$1099/1402232732@53b83897
    */
  val u1 = uncurry(testUncurry)

  /**
    * u2: String => String = $$Lambda$1101/259484490@58c93be3
    */
  val u2 = u1(2, 2)

  /**
    * u3: String = ****
    */
  val u3 = u2("*")

  /**
    * compose: [A, B, C](f: B => C, g: A => B)A => C
    */
  def compose[A,B,C](f:B => C, g: A => B): A => C = (a:A) => f(g(a))

}
