package _2_SyntacticSugar

import cats.implicits.*

// |+| は、Semigroup 型クラスのメソッド combine のシンタックスシュガー
// Semigroup は「結合可能」な構造で、combine は2つの値を結合する操作
// |+| は、2つの値を「加算」や「連結」のような操作
object 加算や連結 {
  def 整数の加算(): Int = {
    val int1 = 1
    val int2 = 2

    int1 |+| int2
  }

  def リストの連結(): Seq[Int] = {
    val list1 = List(1, 2)
    val list2 = List(3, 4)

    list1 |+| list2
  }

  def mapのマージ(): Map[String, Int] = {
    val map1 = Map("a" -> 1)
    val map2 = Map("b" -> 2)

    map1 |+| map2
  }

  def mapのvalueがListのマージ(): Map[String, List[Int]] = {
    val map1 = Map("a" -> List(1, 2))
    val map2 = Map("a" -> List(3, 4))

    map1 |+| map2
  }
}
