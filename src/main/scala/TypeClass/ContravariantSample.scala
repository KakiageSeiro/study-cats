package TypeClass

import cats._
import cats.syntax.all._

// 定義はこちら。反変関手とも呼ぶ。
// Functorの逆であるcontramapをもってる。fがB => Aになっている
// trait Contravariant[F[_]] {
//   def contramap[A, B](fa: F[A])(f: B => A): F[B]
// }

import cats.Show
import cats.instances.string._

case class Person(name: String, age: Int)

// Showは反変関手だよ
val showString: Show[String] = Show.apply[String]
// showString（String型の値を表示する方法を知っている）を使って、Person型の値を表示する方法を作成
// 仮にp.nameがIntだったとしたら、ここでの引数は${p.name.toString}にしなければならない。なぜならStringへの変換を定義することで、表示方法をしっているStringにしたいから
val showPerson: Show[Person] = showString.contramap(p => s"${p.name}, ${p.age}歳")
// このように引数fにB => Aを取ることでなにかやりたいこと(show)の前処理を定義してあげるイメージ。この部分が反変っぽさ

object ContravariantSample {
  // 出力: "田中, 30歳"
  def f: Unit = showPerson.show(Person("田中", 30))
}