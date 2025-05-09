package TypeClass

import cats.data.Kleisli
import cats.syntax.all.*

object KleisliSample {
  // Kleisli[F[_], A, B]はA => F[B]のラッパー

  val parse: Kleisli[Option, String, Int] =
    Kleisli((s: String) =>
      if (s.matches("-?[0-9]+")) Some(s.toInt)
      else None
    )

  val reciprocal: Kleisli[Option, Int, Double] =
    Kleisli((i: Int) =>
      if (i != 0) Some(1.0 / i)
      else None
    )

  val parseAndReciprocal: Kleisli[Option, String, Double] =
    // andThenとかcomposeで合成した結果をKleisliとして返すことができる
    reciprocal.compose(parse)

  // mapとかも生えてて、Kleisli[F[_], A, B]に対してB => Cを渡すとKleisli[F[_], A, C]を返す
}
