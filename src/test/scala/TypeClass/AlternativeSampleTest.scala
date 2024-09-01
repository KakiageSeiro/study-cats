package TypeClass

import org.scalatest.flatspec.AnyFlatSpec

class AlternativeSampleTest extends AnyFlatSpec {

  behavior of "AlternativeSampleTest"

  it should "empty" in {
    val actual: Seq[Int] = Vector()
    assert(actual === AlternativeSample.empty)
  }

  it should "pureOfFive" in {
    val actual: Seq[Int] = Vector(5)
    assert(actual === AlternativeSample.pureOfFive)
  }

  it should "concatenated" in {
    val actual: Seq[Int] = Vector(7, 8)
    assert(actual === AlternativeSample.concatenated)
  }

  it should "concatenated2" in {
    val actual: Seq[Int] = Vector(7, 8)
    assert(actual === AlternativeSample.concatenated2)
  }

  it should "apForVectors" in {
    val actual: Seq[Int] = Vector(14, 16, 12, 13)
    assert(actual === AlternativeSample.apForVectors)
  }
}
