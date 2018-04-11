package fp.chapter6


trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {

  case class Simple(seed: Long) extends RNG {
    override def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = new Simple(newSeed)
      val n = (newSeed >>> 16).toInt
      (n, nextRNG)
    }
  }


  def main(args: Array[String]): Unit = {

    val simple = Simple(100)
    val (v, n) = simple.nextInt
    println(v)
    println(n.nextInt)

  }


}
