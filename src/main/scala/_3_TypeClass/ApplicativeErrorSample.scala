package _3_TypeClass

import cats.ApplicativeError
import cats.instances.either.*

object ApplicativeErrorSample {

  // LeftをStringで表現するEither
  // ここをOptionとかTryに切り替えても、利用者側からは同じようにエラーを扱える?ことがメリットだろうか？
  // OptionとEitherとTryのApplicativeErrorをimplicit valで定義しておけば広い範囲でエラーの扱い方を統一できることもメリット？
  type ErrorOr[A] = Either[String, A]

  // 左はエラー型、右はエラーが起こるかも知れないなにか
  val applicativeError = ApplicativeError[ErrorOr, String]

  // raiseErrorでエラーの生成する
  val error: ErrorOr[Int] = applicativeError.raiseError("Something went wrong")

  // エラー型を処理するときにhandleErrorWithをつかう(E => Aの関数を取るhandleErrorというのもあるよ)
  val handled: ErrorOr[Int] = applicativeError.handleErrorWith(error) {
    case "Something went wrong" => Right(0) // サンプルなのでdefault値を返すってことで
  }

  // 計算を実行
  // ErrorOrがEitherのエイリアスとしてFになっているが、ここでは成功したときに返す型もEitherであることに注意
  // handledはただの変数で失敗しないのでEitherになって返ってくるだけ
  // attempt(fa: F[A]): F[Either[E, A]]
  val attempt: ErrorOr[Either[String, Int]] = applicativeError.attempt(handled)

}
