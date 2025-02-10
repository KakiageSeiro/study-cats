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
  // 変換を実行し、FromString[A] = String => Aを作る。ここで実行もしている
  // foldMapのfold要素は、~~複数の検証ロジックを単一の関数に折り畳む的なやつだと思う~~ progがプログラムの順次処理なので、再起構造としてみなせてそれを折り畳む意味だったぽい
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




// ---------------------------------どのバリデーションがされたかをログにだすケースの使い方？
import cats.data.Const

// Constは第２パラメータを無視する型。ここでは第１パラメータのList[String]はログを累積する用途につかう
// 単なるList[String]ではないのは、ConstがCatsの抽象化(FunctionKとかかな)の仕組みを使うことでロジックの全体でCatsの仕組みを使ってることになり、見やすくなったりするから？
// あとfoldMapするときに累積されるが、累積ロジックはListそのものにはないが、Constで包むことで累積ロジックを書かなくてもよくなってる？
type Log[A] = Const[List[String], A]

val logCompiler = new FunctionK[ValidationOp, Log] {
  def apply[A](fa: ValidationOp[A]): Log[A] = fa match {
    case Size(size) => Const(List(s"size >= $size"))
    case HasNumber => Const(List("has number"))
  }
}

object FreeApplicativeSample_Log {
  def logValidation[A](validation: Validation[A]): List[String] =
    validation.foldMap[Log](logCompiler).getConst

  def logValidation_prog(): Seq[String] =
    logValidation(prog)
    // res2: List[String] = List("size >= 5", "has number")

  // *> は「前の結果を捨てて次に進む」というシーケンス演算子
  def logValidation_size_hasNumber_size(): Seq[String] =
    logValidation(size(5) *> hasNumber *> size(10))
    // res3: List[String] = List("size >= 5", "has number", "size >= 10")

  // || で論理和ととってるが、意味はなくて呼び出されていることだけ確認する
  def logValidation_map2(): Seq[String] =
    logValidation((hasNumber, size(3)).mapN(_ || _))
    // res4: List[String] = List("has number", "size >= 3")
}


// ----------------------------------------並列のログの解釈器を合成できる
import cats.data.Tuple2K

object FreeApplicativeSample_Par_Log {
  type ValidateAndLog[A] = Tuple2K[ParValidator, Log, A]
  val prodCompiler: FunctionK[ValidationOp, ValidateAndLog] = parCompiler and logCompiler
  val prodValidation: ValidateAndLog[Boolean] = prog.foldMap[ValidateAndLog](prodCompiler)
}









