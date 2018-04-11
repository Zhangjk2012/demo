import scala.annotation.tailrec

/**
  * Created by zhangjiangke on 2017/5/22.
  */

class Pet(val name: String)

class Dog(override val name: String) extends Pet(name)

object TestScala {
  def main(args: Array[String]): Unit = {


    val l = collection.mutable.ArrayBuffer[Any]()

    l += "1234"
    l += "66"

    println(l)


//    val f = (x: Double) => math.Pi / 2 - x
//    val cos = f andThen math.sin
//
//    println(cos(12))
//
//
//    println(1 ^ 2)


//    def curry[A,B,C](f: (A,B) => C): A => (B => C) = {
//      (a: A) => (b: B) => f(a,b)
//    }
//
//    def uncurry[A,B,C](f: A=>B=>C): (A,B) => C = {
//      (a: A, b: B) => f(a)(b)
//    }
//
//    def compose[A,B,C](f: B => C, g: A => C) :A => C = {
//      a:A => f(g(a))
//    }

//    @tailrec
//    def fib(n: Int, ret1: Int, ret2: Int) :Int = {
//      if (n == 1) ret1
//      else fib(n - 1, ret2, ret1 + ret2)
//    }
//
//    val ret = fib(5, 0, 1)
//
//    println(ret)
//
//    val l = List(1,2,3,4,5,3)
//
//    val words = "The quick brown fox jumped over the lazy dog".split(' ')
//    val sss = words.sortBy(x => x.length)
//    println(sss)
//
//    def isSorted[A](as: List[A], n: Int, ordered: (A,A)=>Boolean): Boolean = {
//      if (n >= as.size) true
//      else if(!ordered(as(n-1),as(n))) false
//      else isSorted(as, n+1, ordered)
//    }
//
//    val r = isSorted(l, 1, (x: Int,y: Int)=> x < y)
//
//    println(r)


//    val element = new ArrayElement(Array("1","2","3"))
//
//    println(element.height)
//    println(element.width)


//    val set = Set("1","2","3");
//
//
//    val s = set.zipWithIndex
//
//    println(s)


//    val i = 16
//
//    val j = 15
//
//    println(i & j)

//    val n = 0;
//    val m = 100
//
//
//    if (m % 5 == 0 || 100 / n == 10) {
//      println("=====")
//    }


//    println(Integer.toBinaryString(123))

//    val i: Integer = null
//
//
//    val dollar = new Dollars(10);
//
//    print(dollar.getClass())


  }
}


class Dollars(val amount: Int) extends AnyVal {

  override def toString: String = {
    "$" + amount
  }

  def i = 10
}


abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  final def demo() = {}
  def width: Int = if (height == 0) 0 else contents(0).length
}

class ArrayElement(override val contents: Array[String]) extends Element {
}

