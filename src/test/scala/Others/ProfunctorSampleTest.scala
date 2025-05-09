package Others

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProfunctorSampleTest extends AnyFlatSpec with ScalaFutures with Matchers {

  "ProfunctorSample" should "前処理と後処理も実行されている" in {
    val actualFuture: Double = ProfunctorSample.f()
    assert(actualFuture == 5.4)
  }

}
