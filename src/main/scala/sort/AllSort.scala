package sort

/**
  *
  * Created by ZJK on 2018/2/26.
  */
object AllSort extends App {

  val l = Array(1,23,3,5,6,2,9,8)

  def bubbleSort(): Unit = {
    for (i <- 0 until l.length - 1) {
      for (j <- 0 until l.length - 1 - i) {
        if (l(j) > l(j+1)) {
          val tmp = l(j)
          l(j) = l(j + 1)
          l(j + 1) = tmp
        }
      }
    }
  }



  bubbleSort()
  l.foreach(println)
}


