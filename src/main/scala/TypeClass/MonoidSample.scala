package TypeClass

// モノイドはSemigroup(足せるやつ)に単位元を追加したものさ。
// これで畳み込みができるようになる。foldLeftの第一引数が最初からあるってこと。なのでreduceも使えるよ
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object MonoidSample
