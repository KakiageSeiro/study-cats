package DataTypes

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FreeApplicativeSampleTest extends AnyFlatSpec with ScalaFutures with Matchers {

  "作ったvalidator" should "が動く" in {
    assert(!FreeApplicativeSample.validator("1234"))
    assert(FreeApplicativeSample.validator("12345"))
  }


}
