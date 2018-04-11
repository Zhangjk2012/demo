package monoid

trait Monoid[A] {
  def op(a1: A, a2: A): A  // 二元操作
  val zero: A              // 幺元
}


trait WC
case class Stub(chars: String) extends WC
case class Part(lStub: String, words: Int, rStub: String) extends WC

object Monoid {
  val stringConcatMonoid = new Monoid[String] {
    override def op(a1: String, a2: String) = a1 + a2

    override val zero = ""
  }

  val intAdditionMonoid = new Monoid[Int] {
    override def op(a1: Int, a2: Int) = a1 + a2

    override val zero = 0
  }

  val intMultiplicationMonoid = new Monoid[Int] {
    override def op(a1: Int, a2: Int) = a1 * a2

    override val zero = 1
  }

  def reduce[A](as: List[A])(m: Monoid[A]): A = {
    as match {
      case Nil => m.zero
      case h::t => m.op(h, reduce(t)(m))
    }
  }


  def foldMapV[A, B] (iseq: IndexedSeq[A])(m: Monoid[B])(f: A=>B):B = {

    if(iseq.isEmpty) m.zero
    else if (iseq.length == 1) f(iseq.head)
    else {
      val (l, r) = iseq.splitAt(iseq.length / 2)
      m.op(foldMapV(l)(m)(f), foldMapV(r)(m)(f))
    }
  }

  def wcMonoid: Monoid[WC] = new Monoid[WC] {

    override def op(a1: WC, a2: WC): WC = {
      (a1, a2) match {
        case (Stub(l), Stub(r)) => Stub(l + r)
        case (Stub(lb), Part(le, w, rb)) => Part(lb+ le, w, rb)
        case (Part(le, w, rb), Stub(re)) => Part(le, w, rb+re)
        case (Part(le, w, rb), Part(lb, w1, re)) => Part(le, w + (if((rb+lb).isEmpty) 0 else 1) + w1, re)
      }
    }

    override val zero: WC = Stub("")
  }

  def wordCount(ws: String): Int = {
    def wc(c: Char): WC = {
      if (c.isWhitespace) Part("", 0, "")
      else Stub(c.toString)
    }
    def unstub(s: String) = s.length min 1
    foldMapV(ws.toIndexedSeq)(wcMonoid)(wc) match {
      case Stub(c) => 0
      case Part(l,w,r) => unstub(l) + w + unstub(r)
    }
  }

  def main(args: Array[String]): Unit = {
    val words = "the brown fox     is running quickly."
    println(wordCount(words))

  }


}

class In[+A]{
  def fun[B >: A](x:B){}
}



