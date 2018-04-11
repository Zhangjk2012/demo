package scalaztest

import scalaz._
import Scalaz._

case class Logger[LOG, A](log: LOG, value: A) {
  def map[B] (f: A=>B): Logger[LOG, B] = Logger(log, f(value))

  def flatMap[B](f: A=> Logger[LOG, B])(implicit M: Monoid[LOG]): Logger[LOG, B] = {
    val nxLogger = f(value)
    Logger(log |+| nxLogger.log, nxLogger.value)
  }
}

final class LoggerOps[A](a: A) {
  def applyLog[LOG](log: LOG): Logger[LOG, A] = Logger(log, a)
}

object Logger {

  implicit def toLogger[LOG](implicit M: Monoid[LOG]) = new Monad[({type L[x] = Logger[LOG,x]})#L] {
    override def bind[A, B](fa: Logger[LOG, A])(f: A => Logger[LOG, B]): Logger[LOG, B] = fa flatMap f
    override def point[A](a: => A): Logger[LOG, A] = Logger(M.zero, a)
  }

}

object ScalazTest extends App {

  def enterInt(x: Int): Logger[String, Int] = Logger("Entered Int:"+x, x)
  def enterStr(x: String): Logger[String, String] = Logger("Entered String:"+x, x)

  val ss = for {
    a <- enterInt(3)
    b <- enterInt(4)
    c <- enterStr("Result:")
  } yield c + (a * b).shows

  implicit def toLoggerOps[A](a: A) = new LoggerOps[A](a)


  val sss = for {
    a <- 3 applyLog "Entered Int 3"
    b <- 4 applyLog "Entered Int 4"
    c <- "Result:" applyLog "Entered String 'Result'"
  } yield c + (a * b).shows


//  val s = List(1,2,3,4,5,6) foldMap{x => x + 1}
  println(ss)
  println(sss)

}