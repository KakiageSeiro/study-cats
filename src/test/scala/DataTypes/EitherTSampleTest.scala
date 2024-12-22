package DataTypes

import DataTypes.EitherTSample.{divisionProgramAsync, divisionProgramAsync_EitherT}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.*

import scala.concurrent.Future

class EitherTSampleTest extends AnyFlatSpec with ScalaFutures with Matchers {

  "divisionProgramAsync" should "割り切れる" in {
    val actualFuture: Future[Either[String, Double]] = divisionProgramAsync("4", "2")
    // Futureのテストをするときは、まだ終わってない場合はNoneになってしまうのでwhenReadyで待つ
    whenReady(actualFuture) { actual =>
      actual should be (Right(2.0))
    }
  }

  it should "パースできなかった" in {
    val actualFuture: Future[Either[String, Double]] = divisionProgramAsync("a", "b")
    whenReady(actualFuture) { actual =>
      actual should be (Left("a is not a number"))
    }
  }

  "divisionProgramAsync_EitherT" should "割り切れる" in {
    // .valueでEitherTが内部的に保持しているFuture[Either[String, Double]]を取り出す
    // EitherT[Future, String, Double]はFuture[Either[String, Double]]のラッパーなのだ
    val actualFuture: Future[Either[String, Double]] = divisionProgramAsync_EitherT("4", "2").value
    whenReady(actualFuture) { actual =>
      actual should be (Right(2.0))
    }
  }

  it should "パースできなかった" in {
    val actualFuture: Future[Either[String, Double]] = divisionProgramAsync_EitherT("a", "b").value
    whenReady(actualFuture) { actual =>
      actual should be (Left("a is not a number"))
    }
  }
}