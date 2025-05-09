package TypeClass

import TypeClass.eq.EqSample
import cats.Eq
import org.scalatest.flatspec.AnyFlatSpec

class EqSampleTest extends AnyFlatSpec {

  behavior.of("EqSampleTest")

  it should "eq" in {
    // == での比較
    assert(EqSample.p1p2)    // true
    assert(!(EqSample.p1p3)) // false
    assert(!(EqSample.p1p4)) // false

    // ===(Eq) での比較
    assert(EqSample.p1p2eq)    // true
    assert(EqSample.p1p3eq)    // true(定義したロジック通りに動いてる!!)
    assert(!(EqSample.p1p4eq)) // false
  }
}
