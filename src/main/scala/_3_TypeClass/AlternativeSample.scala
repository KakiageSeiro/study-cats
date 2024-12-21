package _3_TypeClass

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

// ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
// ここからはAlternativeを実装したパーサーを作るサンプル
trait Decoder[A] {
  def decode(in: String): Either[Throwable, A]
}

object Decoder {
  def from[A](f: String => Either[Throwable, A]): Decoder[A] =
    new Decoder[A] {
      def decode(in: String) = f(in)
    }
}

// Decoderを使う時は自動的にこのAlternativeもついてくるよ
implicit val decoderAlternative: Alternative[Decoder] = new Alternative[Decoder] {
  def pure[A](a: A) = Decoder.from(Function.const(Right(a)))

  def empty[A] = Decoder.from(Function.const(Left(new Error("No dice."))))

  def combineK[A](l: Decoder[A], r: Decoder[A]): Decoder[A] =
    new Decoder[A] {
      def decode(in: String) = l.decode(in).orElse(r.decode(in))
    }

  def ap[A, B](ff: Decoder[A => B])(fa: Decoder[A]): Decoder[B] =
    new Decoder[B] {
      def decode(in: String) = fa.decode(in) ap ff.decode(in)
    }
}

object Alternativeを実装したDecoderを使ってみる {
  // どのようにパース(デコード)するかを定義する関数2個
  def parseInt(s: String): Either[Throwable, Int] = Either.catchNonFatal(s.toInt)

  def parseIntFirstChar(s: String): Either[Throwable, Int] = Either.catchNonFatal(2 * Character.digit(s.charAt(0), 10))

  // まずparseIntをやって無理だったらparseIntFirsChatをやるパーサー(デコーダー)を作る
  val decoder: Decoder[Int] = Decoder.from(parseInt) <+> Decoder.from(parseIntFirstChar)
}

// ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
// Alternativeはseparate()でパーティショニング(分類みたいな)ができる
// 分類できるものはよくわからんがEitherとかtuple2とか

object Alternativeでリクエストの結果をパーティショニング {
  // なんかリクエストがきたら成功したり失敗したりすることをシミュレートする関数
  def requestResource(a: Int): Either[(Int, String), (Int, Long)] = {
    if (a % 4 == 0) Left((a, "Bad request"))
    else if (a % 3 == 0) Left((a, "Server error"))
    else Right((a, 200L))
  }

  // たくさんリクエストをして、結果を分類してみる
  val partitionedResults: (Vector[(Int, String)], Vector[(Int, Long)]) =
    ((requestResource).pure[Vector] ap Vector(5, 6, 7, 99, 1200, 8, 22)).separate
  // partitionedResults: (Vector[(Int, String)], Vector[(Int, Long)]) = (
  //   Vector(
  //     (6, "Server error"),
  //     (99, "Server error"),
  //     (1200, "Bad request"),
  //     (8, "Bad request")
  //   ),
  //   Vector((5, 200L), (7, 200L), (22, 200L))
  // )
}
