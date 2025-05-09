package TypeClass.eq

import cats.*
import cats.syntax.all.*

object EqSample {
  case class Person(name: String, age: Int)

  // Person型のEqインスタンスを定義(ここにPerson独自の比較ロジックを書く)
  implicit val personEq: Eq[Person] = Eq.instance { (p1, p2) =>
    // 名前だけ合ってればOK!!
    p1.name === p2.name
  }

  val p1 = Person("Alice", 30)
  val p2 = Person("Alice", 30)
  val p3 = Person("Alice", 25) // 年齢がちがうよ
  val p4 = Person("Bob", 30)   // 名前がちがうよ

  // == での比較
  def p1p2: Boolean = {
    p1 == p2 // true
  }

  def p1p3: Boolean = {
    p1 == p3 // false
  }

  def p1p4: Boolean = {
    p1 == p4 // false
  }

  // ===(Eq) での比較
  def p1p2eq: Boolean = {
    p1 === p2 // true
  }

  def p1p3eq: Boolean = {
    p1 === p3 // true(定義したロジック通りに動いてる!!)
  }

  def p1p4eq: Boolean = {
    p1 === p4 // false
  }
}
