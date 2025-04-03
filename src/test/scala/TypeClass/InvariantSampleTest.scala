package TypeClass

import org.scalatest.flatspec.AnyFlatSpec

class InvariantSampleTest extends AnyFlatSpec {

  behavior of "AlternativeSampleTest"

  it should "combine" in {
    val actual: Wallet = Wallet(1499)
    assert(actual === InvariantSample.combine())
  }
}
