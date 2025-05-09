package TypeClass

import TypeClass.eq.EqSample
import TypeClass.show.ShowSample
import cats.Eq
import org.scalatest.flatspec.AnyFlatSpec

class ShowSampleTest extends AnyFlatSpec {

  behavior.of("ShowSampleTest")

  it should "show" in {
    // == での比較
    assert(ShowSample.showToString == "Person(Alice,30)")
    assert(ShowSample.showSample == "Aliceさんは30歳です")
  }
}
