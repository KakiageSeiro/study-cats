package SyntacticSugar

import org.scalatest.flatspec.AnyFlatSpec

class 無視して続けるTest extends AnyFlatSpec {
  behavior of "無視して続けるTest"

  it should "左は無視" in {
    val actual: Int = 無視して続ける.左は無視()
    assert(actual === 111)
  }
}
