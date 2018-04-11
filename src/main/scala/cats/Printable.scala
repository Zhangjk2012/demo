package cats

/**
  *
  * Created by ZJK on 2017/12/14.
  */
trait Printable[A] {
  def format(v: A): String
}

object PrintableInstances {

  implicit val intPrintable = new Printable[Int] {
    override def format(v: Int): String = v.toString
  }

  implicit val StrPrintable = new Printable[String] {
    override def format(v: String): String = v
  }

  implicit val catPrintable = new Printable[Cat] {
    override def format(cat: Cat): String = {
      val name = Printable.format(cat.name)
      val age = Printable.format(cat.age)
      val color = Printable.format(cat.color)
      s"$name is a $age year-old $color cat."
    }
  }

}

object Printable {
  def format[A](input: A)(implicit p: Printable[A]) = {
    p.format(input)
  }

  def print[A](input: A)(implicit p: Printable[A]) = {
    println(format(input))
  }
}

final case class Cat(
                    name: String,
                    age: Int,
                    color: String
                    )

object PrintableSyntax {
  implicit class PrintOps[A](v: A) {
  }
}

object Test extends App {

//  import PrintableInstances.catPrintable
//
//  val cat = Cat("AGE", 1, "Black-White")
//
//  Printable.print(cat)


  val seq = Seq[Int]()

//  for(i <- seq) {
//    println("====")
//    println(i)
//  }


  val ss = seq map {
    println("A")
    "w"
  }


  val s = seq map { i =>
    println("B")
    i + ""
  }
  println(ss)

}
