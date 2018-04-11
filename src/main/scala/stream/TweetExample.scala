package stream

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import stream.Main.system

import scala.concurrent.Future

final case class Author(handle: String)

final case class HashTag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashTags: Set[HashTag] = body.split(" ").collect{
    case t if t.startsWith("#") => HashTag(t.replaceAll("[^#\\w]", ""))
  }.toSet
}

object TweetExample extends App {
  val akkaTag = HashTag("#akka")
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
  implicit val ec = system.dispatcher

//
//  val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ => 1)
//
//  val sumSink: Sink[Int, Future[Int]] = Sink.fold(0)(_+_)
//
//  val counterGraph: RunnableGraph[Future[Int]] = tweets.via(count).toMat(sumSink)(Keep.right)
//  val sum = counterGraph.run();
//
//  sum.foreach(c => println(c))




  // filter out tags containing akka
//  val authors: Source[Author, NotUsed] = tweets.filter(_.hashTags.contains(akkaTag)).map(_.author)
//
//  authors.runWith(Sink.foreach(println))
//
//  authors.runForeach(println)
//
//  val hashTags: Source[HashTag, NotUsed] = tweets.mapConcat(_.hashTags.toList)

//  val writeAuthors: Sink[Author, NotUsed] = ???
//  val writeHashTags: Sink[HashTag, NotUsed] = ???
//
//
//  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
//    import GraphDSL.Implicits._
//    val bcast = b.add(Broadcast[Tweet](2))
//    tweets ~> bcast.in
//    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
//    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashTags.toList) ~> writeHashTags
//    ClosedShape
//  })




  val topHeadSink = Sink.head[Int]
  val bottomHeadSink = Sink.head[Int]
  val sharedDoubler = Flow[Int].map(_ * 2)

  val g = RunnableGraph.fromGraph(GraphDSL.create(topHeadSink, bottomHeadSink)((_,_)) { implicit builder =>
    (topHS, bottomHS) =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))
      Source.single(1) ~> broadcast.in

      broadcast.out(0) ~> sharedDoubler ~> topHS.in
      broadcast.out(1) ~> sharedDoubler ~> bottomHS.in
      ClosedShape
  })

  val result: (Future[Int], Future[Int]) = g.run()

  result._1.onComplete(s => println(s))
  result._2.onComplete(s => println(s))



//  val done: Future[Done] = tweets.map(_.hashTags)            // Get all sets of hashtags ...
//    .reduce(_ ++ _)                 // ... and reduce them to a single set, removing duplicates across all tweets
//    .mapConcat(identity)            // Flatten the stream of tweets to a stream of hashtags
//    .map(_.name.toUpperCase)        // Convert all hashtags to upper case
//    .runWith(Sink.foreach(println)) // Attach the Flow to a Sink that will finally print the hashtags
//
//  done.onComplete(_=> system.terminate)

}
