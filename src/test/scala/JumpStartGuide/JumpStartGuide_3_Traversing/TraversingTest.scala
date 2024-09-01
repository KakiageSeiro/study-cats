package JumpStartGuide.JumpStartGuide_3_Traversing

import JumpStartGuide.JumpStartGuide_3_Traversing.{Traversing, User}
import org.scalatest.concurrent.{ScalaFutures, TimeLimits}
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuiteLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Try}

class TraversingTest extends AnyFlatSpec with Diagrams with TimeLimits with ScalaFutures {
  "updateUsersで" should "Future[List[User]]が返ってくる" in {
    val users = List(User("tako"), User("ika"), User("kani"))
    val futureString: Future[List[User]] = Traversing.updateUsers(users)
    futureString.map { userList =>
      assert(userList.length == 3)
      assert(userList(0).name.toCharArray.forall(_.isLetterOrDigit))
      assert(userList(1).name.toCharArray.forall(_.isLetterOrDigit))
      assert(userList(2).name.toCharArray.forall(_.isLetterOrDigit))
    }
  }

  "sequenceで" should "Future[List[String]]が返ってくる" in {
    val fls = Traversing.sequence()
    fls.map { stringList =>
      assert(stringList.length == 2)
      assert(stringList(0) == "hello")
      assert(stringList(1) == "world")
    }
  }
}
