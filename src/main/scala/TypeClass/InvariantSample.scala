package TypeClass

import cats.*
import cats.syntax.all.*

// 定義はこちら。不変関手とも呼ぶ。
// trait Invariant[F[_]] {
//   def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
// }

case class Wallet(amount: Int)

object InvariantSample {
  def walletToInt: Wallet => Int = _.amount

  // Int型からWallet型への変換はなぜか手数料1円が取られるんだな
  def intToWallet: Int => Wallet = amount => Wallet(if (amount > 0) amount - 1 else 0)

  // Int型のMonoidを使ってWallet型のMonoidを作成
  implicit val monoidWallet: Monoid[Wallet] = Monoid[Int].imap(intToWallet)(walletToInt)

  def combine(): Wallet = {
    val wallet1 = Wallet(1000)
    val wallet2 = Wallet(500)

    // |+| で結合するが、Walletでモノイドを定義していないのに結合できるってわけ
    // このときIntのMonoidが使われて1500になり、Walletにするときに+1される
    wallet1 |+| wallet2
  }
}
