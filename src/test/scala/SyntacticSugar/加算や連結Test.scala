package SyntacticSugar

import org.scalatest.flatspec.AnyFlatSpec

class 加算や連結Test extends AnyFlatSpec {

  behavior of "加算や連結Test"

  it should "整数の加算" in {
    val actual: Int = 加算や連結.整数の加算()
    assert(actual === 3)
  }

  it should "リストの連結" in {
    val actual: Seq[Int] = 加算や連結.リストの連結()
    assert(actual === List(1, 2, 3, 4))
  }

  it should "mapのマージ" in {
    val actual: Map[String, Int] = 加算や連結.mapのマージ()
    assert(actual === Map("a" -> 1, "b" -> 2))
  }

  it should "mapのvalueがListのマージ" in {
    val actual: Map[String, List[Int]] = 加算や連結.mapのvalueがListのマージ()
    assert(actual === Map("a" -> List(1, 2, 3, 4)))
  }
}
