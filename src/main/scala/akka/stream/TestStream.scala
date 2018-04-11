import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

final case class Author(handle: String)

final case class Hashtag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] =
    body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}


object TestStream {

  def main(args: Array[String]): Unit = {
    val akkaTag = Hashtag("#akka")

    val tweets: Source[Tweet, NotUsed] = Source(
        Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
        Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
        Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
        Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
        Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
        Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
        Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
        Nil)

    implicit val system = ActorSystem("reactive-tweets")
    implicit val materializer = ActorMaterializer()

//    testGraphZipWith(system)

//    tweets.buffer(10, OverflowStrategy.dropHead)


//    otherStream(materializer)

    val future = tweets
      .map(_.hashtags)
      .filterNot(_.contains(akkaTag))
      .mapConcat(identity)
      .map(_.name.toUpperCase)
      .runWith(Sink.foreach(println))
//
//
//
//
//
    def lineSink(filename: String): Sink[String, Future[IOResult]] =
      Flow[String]
        .map(s => ByteString(s + "\n"))
        .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
//
//    implicit val dispatcher = system.dispatcher
//    future.onComplete(_=>system.terminate())
  }

  def otherStream(materializer: ActorMaterializer)(implicit system: ActorSystem): Unit = {
    val source: Source[Int, NotUsed] = Source(1 to 10)
    val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

    def lineSink(filename: String): Sink[String, Future[IOResult]] =
          Flow[String]
            .map(s => ByteString(s + "\n"))
            .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

//    val result: Future[IOResult] =
//      factorials
//        .map(_.toString())
//        .runWith(lineSink("D:/factorials.txt"))(materializer)

    factorials
      .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
      // 节流实现，一秒一个，最大也是一个。
      .throttle(1, 1.second, 1, ThrottleMode.shaping)
      .runForeach(println)(materializer)



//    implicit val dispatcher = system.dispatcher
//    result.onComplete(x=> {
//      val s = x.get
//      println(s)
//      system.terminate()
//    })
  }


  def otherStream1(materializer: ActorMaterializer)(implicit system: ActorSystem): Unit = {

    val tweets: Source[Tweet, NotUsed] = Source(
      Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
        Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
        Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
        Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
        Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
        Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
        Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
        Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
        Nil)

    val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ => 1)

    val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

    val counterGraph: RunnableGraph[Future[Int]] =
      tweets
        .via(count)
        .toMat(sumSink)(Keep.right)

    val sum: Future[Int] = counterGraph.run()(materializer)
    implicit val dispatcher = system.dispatcher
    sum.foreach(c => println(s"Total tweets processed: $c"))
  }

  def otherStream3(): Unit = {
//    val source = Source(1 to 10)
//    val sink = Sink.fold[Int, Int](0)(_ + _)
//
//    // connect the Source to the Sink, obtaining a RunnableGraph
//    val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)
//
//    // materialize the flow and get the value of the FoldSink
//    val sum: Future[Int] = runnable.run()
  }


  def testGraph(system: ActorSystem)(implicit materializer: ActorMaterializer): Unit = {
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val in = Source(1 to 10)
      val out = Sink.foreach(println)
      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))
      val f1, f2, f3, f4 = Flow[Int].map(_ + 10)
      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
      bcast ~> f4 ~> merge
      ClosedShape
    })
    g.run()
  }

  def testGraph1(system: ActorSystem)(implicit materializer: ActorMaterializer): Unit = {

    val topHeadSink = Sink.head[Int]
    val bottomHeadSink = Sink.head[Int]
    val out = Sink.foreach(println)
    val sharedDoubler = Flow[Int].map(_ * 2)


    val g = RunnableGraph.fromGraph(GraphDSL.create(topHeadSink, bottomHeadSink)((_,_)){
      implicit builder => (topHs, bottomHs) => {
        import GraphDSL.Implicits._
        val out = Sink.foreach(println)
        val broadcast = builder.add(Broadcast[Int](2))
        Source.single(1) ~> broadcast.in
        broadcast.out(0) ~> sharedDoubler ~> topHs.in
        broadcast.out(1) ~> sharedDoubler ~> bottomHs.in
        ClosedShape
      }
    })
    g.run()

    val shape = topHeadSink.shape
    println(shape)

  }


  def testGraphZipWith(system: ActorSystem)(implicit materializer: ActorMaterializer): Unit = {
    val pickMaxOfThree = GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      def max(x:Int,y:Int) = math.max(x,y)

      val zip1 = b.add(ZipWith[Int, Int, Int](max))
      val zip2 = b.add(ZipWith[Int, Int, Int](math.max))
      val zip3 = b.add(ZipWith[Int, Int, Int](math.max))

      zip3.out ~> zip2.in1
      zip1.out ~> zip2.in0

      UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip3.in0, zip3.in1)
    }

    val resultSink = Sink.head[Int]

    val g = RunnableGraph.fromGraph(GraphDSL.create(resultSink){
      implicit b => sink => {
        import GraphDSL.Implicits._
        val pm3 = b.add(pickMaxOfThree)
        Source.single(100) ~> pm3.in(0)
        Source.single(2) ~> pm3.in(1)
        Source.single(3) ~> pm3.in(2)
        Source.single(101) ~> pm3.in(3)
        pm3.out ~> sink.in
        ClosedShape
      }
    })
    Zip
    val max: Future[Int] = g.run()

    val result = Await.result(max, 300.millis)
    println(result)

  }


}

