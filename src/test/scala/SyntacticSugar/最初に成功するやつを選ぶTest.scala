package SyntacticSugar

import org.scalatest.flatspec.AnyFlatSpec

class 最初に成功するやつを選ぶTest extends AnyFlatSpec {
  "opt1が成功()で" should "Some(1)が返ってくる" in {
    val actual: Option[Int] = 最初に成功するやつを選ぶ.opt1が成功()
    assert(actual === Some(1))
  }

  "optNoneが失敗()で" should "Some(2)が返ってくる" in {
    val actual: Option[Int] = 最初に成功するやつを選ぶ.optNoneが失敗()
    assert(actual === Some(2))
  }

  "両方失敗()で" should "Noneが返ってくる" in {
    val actual: Option[Int] = 最初に成功するやつを選ぶ.両方失敗()
    assert(actual === None)
  }
}
