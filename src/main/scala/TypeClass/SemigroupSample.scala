package TypeClass

import cats.kernel

// セミグループは"足せる"ってことさ
trait Semigroup[A] {
  def combine(x: A, y: A): A
  // combine(x, combine(y, z)) = combine(combine(x, y), z) のように、計算の順序は関係ないので並列にしたりできる
}

object SemigroupSample {
  // Intにおいて"足す"がなになのかを定義する
  implicit val intAdditionSemigroup: Semigroup[Int] = _ + _

  val x = 1
  val y = 2
  intAdditionSemigroup.combine(x, y)
  // Int = 3

  // 専用のシンタックスがあるぞ
  import cats.syntax.all.*
  1 |+| 2
  // res4: Int = 3
}
