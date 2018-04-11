package cats

import cats.instances.int._

import cats.syntax.show._

/**
  *
  * Created by ZJK on 2017/12/19.
  */
object ShowTest extends App {
  val showInt: Show[Int] = Show.apply[Int]

  println(showInt.show(1234))

  println(1234.show)

}
