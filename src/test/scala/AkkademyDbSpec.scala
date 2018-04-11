import akka.{AkkademyDb, SetRequest}
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._

/**
  * Created by zhangjiangke on 2017/6/8.
  */
class AkkademyDbSpec extends FunSpecLike with Matchers{

  implicit val system = ActorSystem()

  implicit val timeout = Timeout(5 seconds)

  describe("akkademyDb") {
    describe("given SetRequest") {
//      it("should place key/value into map") {
//        val actor = TestActorRef(new AkkademyDb)
//        actor ! SetRequest("key", "value")
//        val akkademyDb = actor.underlyingActor
//        akkademyDb.map.get("key") should equal (Some("value"))
//
//      }
    }
  }


}
