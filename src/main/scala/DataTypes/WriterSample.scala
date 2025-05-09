package DataTypes

import cats.Id
import cats.data.Writer

import scala.math.sqrt

// Writerはログと値をもつタプルのラッパー
// https://typelevel.org/cats/datatypes/writer.html の一番したに書いてあったサンプルがパッと理解できなかったので書く

val writer1: Writer[String, Double]           = Writer.value[String, Double](5.0).tell("Initial value ")
val writer2: Writer[String, Double => Double] = Writer("sqrt ", (i: Double) => sqrt(i))
val writer3: Double => Writer[String, Double] = (x: Double) => Writer("add 1 ", x + 1)
val writer4: Writer[String, Double => Double] = Writer("divided by 2 ", (x: Double) => x / 2)

val writer5: Writer[String, Double => Double] = {
  // ログだけ取ったやつ
  val written: Id[String] = writer3(0).written

  // たし算関数だけ取ったやつ
  val doubleToValue: Double => Id[Double] = (x: Double) => writer3(x).value

  Writer[String, Double => Double](
    written,
    doubleToValue
  )
}

object WriterSample {
  // Pay attention on the ordering of the logs
  // ログの順序に注意

  writer1
    .ap(writer2)
    .flatMap(writer3(_))
    .ap(writer4)
    .map(_.toString)
    .run
  // res10: cats.package.Id[(String, String)] = (
  //   "divided by 2 sqrt Initial value add 1 ",
  //   "1.618033988749895"
  // )

  // 割り算(writer4) 平方根(writer2) 初期化(writer1) たし算(writer3) という順序でログが出てる！！
  // これは"ログの記録"という処理をするのに、apなどのapplicativeを使ってるのが悪い
  // writer1_ログと値.ap(writer2_ログと平方根の関数) で引数が先に評価されるのは直感に合う
  // というわけでモナドを使うのが下の例ってわけ

  /// >>>という記号(andThenと同じ)用
  import cats.syntax.compose.*

  (
    for {
      initialValue <- writer1
      sqrt         <- writer2
      addOne       <- writer5
      divideBy2    <- writer4
    } yield (sqrt >>> addOne >>> divideBy2)(initialValue)
  ).run

  // res11: cats.package.Id[(String, Double)] = (
  //   "Initial value sqrt add 1 divided by 2 ", // yieldで書いた順序の通りにログが出てるぜ！！
  //   1.618033988749895
  // )
}
