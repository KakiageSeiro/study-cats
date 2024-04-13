package JumpStartGuide_2_Tuples

import org.scalatest.concurrent.{ScalaFutures, TimeLimits}
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.time.{Millis, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Try}

class TuplesTest extends AnyFlatSpec with Diagrams with TimeLimits with ScalaFutures {

  "intFutureで" should "Future[Int]が返ってくる" in {
    val futureInt = Tuples.intFuture
    futureInt.map { value =>
      assert(value >= 0 && value < 100)
    }
  }

  "stringFutureで" should "Future[String]が返ってくる" in {
    val futureString = Tuples.stringFuture
    futureString.map { contents =>
      assert(contents.length == 10)
      assert(contents.toCharArray.forall(_.isLetterOrDigit))
    }
  }

  "userFutureで" should "Future[User]が返ってくる" in {
    val futureUser = Tuples.userFuture
    futureUser.map { user =>
      assert(user.name == "John Doe")
    }
  }

  "intFutureFailureで" should "エラーメッセージが返ってくる" in {
    val futureInt = Tuples.intFutureFailure
    val result: Try[Int] = futureInt.value.get

    result match {
      case Failure(ex: Throwable) =>
        assert(ex.isInstanceOf[RuntimeException])
        assert(ex.getMessage == "何かが失敗1")
      case _ =>
        fail("Failureなはずなのにそうじゃなかった")
    }
  }

  "stringFutureFailureで" should "エラーメッセージが返ってくる" in {
    val futureInt = Tuples.stringFutureFailure
    val result: Try[String] = futureInt.value.get

    result match {
      case Failure(ex: Throwable) =>
        assert(ex.isInstanceOf[RuntimeException])
        assert(ex.getMessage == "何かが失敗2")
      case _ =>
        fail("Failureなはずなのにそうじゃなかった")
    }
  }

  "processで" should "ProcessingResultが返ってくる" in {
    val value = 42
    val contents = "Hello"
    val user = new User("Jane")
    val result = Tuples.process(value, contents, user)
    assert(result.value == value)
    assert(result.contents == contents)
    assert(result.userName == user.name)
  }

  "processAsyncで" should "Future[ProcessingResult]が返ってくる" in {
    val futureResult = Tuples.processAsync
    futureResult.map { result =>
      assert(result.value >= 0 && result.value < 100)
      assert(result.contents.length == 10)
      assert(result.contents.forall(_.isLetterOrDigit))
      assert(result.userName == "John Doe")
    }
  }

  "processAsync2で" should "Future[ProcessingResult]が返ってくる" in {
    val futureResult = Tuples.processAsync2
    futureResult.map { result =>
      assert(result.value >= 0 && result.value < 100)
      assert(result.contents.length == 10)
      assert(result.contents.forall(_.isLetterOrDigit))
      assert(result.userName == "John Doe")
    }
  }

  "processAsync3で" should "Future[ProcessingResult]が返ってくる" in {
    val futureResult = Tuples.processAsync3
    futureResult.map { result =>
      assert(result.value >= 0 && result.value < 100)
      assert(result.contents.length == 10)
      assert(result.contents.forall(_.isLetterOrDigit))
      assert(result.userName == "John Doe")
    }
  }

  "processAsync2IntFailureで" should "IntFutureFailureのエラーが返ってくる" in {
    val futureResult = Tuples.processAsync2IntFailure
    val result = futureResult.value.get

    result match {
      case Failure(ex: Throwable) =>
        assert(ex.isInstanceOf[RuntimeException])
        assert(ex.getMessage == "何かが失敗1")
      case _ =>
        fail("Future should have failed")
    }
  }

  // このテストではintかstringのどちらかが先に失敗したほうのエラー"だけ"が返ってくるはず(mapNはfail fastとのこと)だが、なぜかintが必ず返ってくる。実行のたびに変化しても良いはずだが…
  // それにもやっとしたのでテストで非同期処理を待機しようとしたが、上手くいかなかった…
  "processAsync2IntAndStringFailureで" should "IntFutureFailureのエラーが返ってくる" in {
    val futureResult = Tuples.processAsync2IntAndStringFailure
    val result = futureResult.value.get

    result match {
      case Failure(ex: Throwable) =>
        assert(ex.isInstanceOf[RuntimeException])
        assert(ex.getMessage == "何かが失敗1")
      case _ =>
        fail("Future should have failed")
    }
  }


  // 非同期処理を待機しようとしてもがいたコード
//  "processAsync2IntAndStringFailureで" should "IntとStringのFutureFailureのエラーが返ってくる" in {
//    val futureResult: Future[Tuples.ProcessingResult] = Tuples.processAsync2IntAndStringFailure
//    assert(futureResult.isReadyWithin(Span(10000, Millis))) // 1秒以内に完了することの検証
//    assert(futureResult.failed.futureValue.isInstanceOf[RuntimeException])
//    assert(futureResult.failed.futureValue.getMessage == "何かが失敗2")
//  }



}
