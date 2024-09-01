package TypeClass

import cats.Alternative
import cats.syntax.all.*

object AlternativeSample {
  val empty: Vector[Int] = Alternative[Vector].empty[Int]
  // Vector()

  val pureOfFive: Vector[Int] = 5.pure[Vector]
  // Vector(5)

  // <+>は「最初に成功するやつを選ぶ」かと思ったけど、Optionの<+>の実装が「最初に成功するやつを選ぶ」っぽい。VectorだとふつうにaddAllってこと？
  val concatenated: Vector[Int] = 7.pure[Vector] <+> 8.pure[Vector]
  // Vector(7, 8)

  // それだと|+|はどうなる？って思ったけどVector(7, 8)だった。Vectorだと同じ実装ってことかも
  val concatenated2: Vector[Int] = 7.pure[Vector] |+| 8.pure[Vector]

  // よくよくドキュメントをみたら、Alternativeを実装したパーサーのサンプルで以下のようにcombineK(<+>がシュガーシンタックス)をユーザー定義したから「最初に成功するやつを選ぶ」挙動にしている
  // def combineK[A](l: Decoder[A], r: Decoder[A]): Decoder[A] =
  //     new Decoder[A] {
  //       def decode(in: String) = l.decode(in).orElse(r.decode(in))
  //     }
  // というわけで、サンプルのパーサーは自分で定義した挙動、Optionは最初に成功するやつを選ぶような実装、VectorはaddAllって事だと思う

  val double: Int => Int = _ * 2
  // <function1>

  val addFive: Int => Int = _ + 5
  // <function1>

  val apForVectors: Vector[Int] = (double.pure[Vector] <+> addFive.pure[Vector]) ap concatenated
  // Vector(14, 16, 12, 13)
  // 7 * 2 = 14
  // 8 * 2 = 16
  // 7 + 5 = 12
  // 8 + 5 = 13
}