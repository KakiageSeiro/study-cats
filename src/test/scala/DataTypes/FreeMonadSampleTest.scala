package DataTypes

import cats.data.Const
import cats.syntax.all.*
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future


class FreeMonadSampleTest extends AnyFlatSpec with ScalaFutures with Matchers {

  "kvsを動かす一連の操作が" should "が動く" in {
    assert(FreeMonadSample.result.contains(14))
  }

  "State版" should "が動く" in {
    assert(FreeMonadSample2.result._1 == Map("wild-cats" -> 14))
    assert(FreeMonadSample2.result._2.contains(14))
  }

}
