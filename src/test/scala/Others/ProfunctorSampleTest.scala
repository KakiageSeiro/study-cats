package Others

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProfunctorSampleTest extends AnyFlatSpec with ScalaFutures with Matchers{

  "divisionProgramAsync" should "割り切れる" in {
    val actualFuture: Double = ProfunctorSample.f()
    assert(actualFuture == 5.4)
  }


}
