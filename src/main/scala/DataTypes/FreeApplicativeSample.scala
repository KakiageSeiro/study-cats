package DataTypes

class FreeApplicativeSample

// 文字列を検証するためのEDSLのサンプル
// EDSL = Embedded Domain-Specific Languages ホストプログラミング言語に組み込まれた小さなプログラミング言語

// 文字列検証の代数を定義
sealed abstract class ValidationOp[A]
case class Size(size: Int) extends ValidationOp[Boolean] // サイズの検証用
case object HasNumber extends ValidationOp[Boolean] // 数字を含んでいることの検証用


// FreeApplicativeを作るよ
import cats.free.FreeApplicative
import cats.free.FreeApplicative.lift

type Validation[A] = FreeApplicative[ValidationOp, A]
def size(size: Int): Validation[Boolean] = lift(Size(size))
val hasNumber: Validation[Boolean] = lift(HasNumber)

// 糖衣構文。多分"&&"でバリデーション両方やれるよって事だと思う
import cats.syntax.all.*

// 最終的な検証処理の実装
// 実装は「FreeApplicativeで定義されたDSL」で書かれている
val prog: Validation[Boolean] = (size(5), hasNumber).mapN { case (l, r) => l && r }

// ------------------------------------------------------------ここから解釈器の実装
import cats.arrow.FunctionK

// 文字列を入力として受け取る関数
type FromString[A] = String => A

// FunctionKは左から右への型変換を、型安全にできる君らしい
// prog内部の各ValidationOpをcompilerを使ってFromStringに変換するコンパイラ
val compiler = new FunctionK[ValidationOp, FromString] {
  def apply[A](fa: ValidationOp[A]): FromString[A] = str =>
    fa match {
      case Size(size) => str.size >= size
      case HasNumber  => str.exists(c => "0123456789".contains(c))
    }
}

object FreeApplicativeSample {
  // 変換を実行し、FromString[A] = String => Aを作る
  // foldMapのfold要素は、複数の検証ロジックを単一の関数に折り畳む的なやつだと思う
  // これで文字列を簡単に検証できる
  val validator: FromString[Boolean] = prog.foldMap[FromString](compiler)
}







// ----------------------------------並列処理ができる
import cats.data.Kleisli

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Kleisli[Future, String, A] は String => Future[A] のラッパー
type ParValidator[A] = Kleisli[Future, String, A]

val parCompiler = new FunctionK[ValidationOp, ParValidator] {
  def apply[A](fa: ValidationOp[A]): ParValidator[A] = Kleisli { str =>
    fa match {
      case Size(size) => Future { str.size >= size }                         // ここがFutureだから並列にできるぞ！
      case HasNumber => Future { str.exists(c => "0123456789".contains(c)) } // ここがFutureだから並列にできるぞ！
    }
  }
}

// ここでfoldMapする時に、Futureだから並列に処理できる
val parValidator: ParValidator[Boolean] = prog.foldMap[ParValidator](parCompiler)
