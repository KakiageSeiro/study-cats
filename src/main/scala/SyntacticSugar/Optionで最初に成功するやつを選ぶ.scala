package SyntacticSugar

import cats.implicits.*

// <+> は、Alternative 型クラス(SemigroupKかも)のメソッド combineK のシンタックスシュガー
// Alternative は、Applicative と MonoidK を組み合わせたもので、コレクションやオプションのような型に対して「選択的な結合(n個あるけどどれか選ぶみたいな感じ)」操作を提供
// <+> は、2つの値の間で「最初に成功するものを選ぶ」という挙動
// 今までifや三項演算子でやってたことが簡単に
// Optionは失敗した情報がないので捨てていいことを、良い感じに利用してる→Eitherには向かなそう
object Optionで最初に成功するやつを選ぶ {
  private val opt1 = Option(1)
  private val opt2 = Option(2)
  private val optNone: Option[Int] = None

  def opt1が成功(): Option[Int] = {
    opt1 <+> opt2
  }

  def optNoneが失敗(): Option[Int] = {
    optNone <+> opt2
  }

  def 両方失敗(): Option[Int] = {
    optNone <+> optNone
  }
}
