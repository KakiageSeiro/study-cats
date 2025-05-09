package DataTypes

// Freeモナドを使う動機
// ・計算を純粋で不変の値として表現したい。
// ・プログラムの作成と実行を分離する
// ・さまざまな実行方法をサポートできるようにする。

// KVSの操作を定義
sealed trait KVStoreA[A]
case class Put[T](key: String, value: T) extends KVStoreA[Unit]
case class Get[T](key: String)           extends KVStoreA[Option[T]]
case class Delete(key: String)           extends KVStoreA[Unit]

// 上でつくった代数的データをFreeing(解法？)するステップ
// 1. Free[_]とKVStoreA[_]に基づいて型を作成する。
// 2. KVStore[_] のスマート・コンストラクタを、liftF を使用して作成する。
// 3. キーバリューDSL操作からプログラムを構築する。
// 4. DSL操作のプログラムのコンパイラを作る。
// 5. コンパイルしたプログラムを実行する。

// 1. Free[_]とKVStoreA[_]に基づいて型を作成する。----------------------------------------------------
import cats.free.Free

type KVStore[A] = Free[KVStoreA, A]

// 2. KVStore[_] のスマート・コンストラクタを、liftF を使用して作成する。-----------------------------
import cats.free.Free.liftF

def put[T](key: String, value: T): KVStore[Unit] =
  liftF[KVStoreA, Unit](Put[T](key, value))

def get[T](key: String): KVStore[Option[T]] =
  liftF[KVStoreA, Option[T]](Get[T](key))

def delete(key: String): KVStore[Unit] =
  liftF(Delete(key))

// getしてputするよ
def update[T](key: String, f: T => T): KVStore[Unit] =
  for {
    vMaybe <- get[T](key)
    _      <- vMaybe.map(v => put[T](key, f(v))).getOrElse(Free.pure(()))
  } yield ()

// 3. キーバリューDSL操作からプログラムを構築する。---------------------------------------------------------
// これは実際に上で定義したプログラムをつかって実行する流れを書く。ユースケースとかの名前が知ってる概念に近いかも？
def program: KVStore[Option[Int]] =
  for {
    _ <- put("wild-cats", 2)
    _ <- update[Int]("wild-cats", (_ + 12))
    _ <- put("tame-cats", 5)
    n <- get[Int]("wild-cats")
    _ <- delete("tame-cats")
  } yield n

// 4. DSL操作のプログラムのコンパイラを作る。---------------------------------------------------------------------------------------

// Free[_]はプログラミング言語の中のプログラミング言語
// だから、他のプログラミング言語と同じように、抽象言語を有効な言語にコンパイルし、それを実行する必要がある。

// コンパイル
// 自然変換は、F[_]とG[_]のような型の変換
// この特定の変換は、FunctionK[F,G]を使う。F ~> Gと書ける
import cats.arrow.FunctionK
import cats.{Id, ~>}

import scala.collection.mutable

// 型が正しく指定されていない場合、プログラムがクラッシュするぞ
// Idはモナドを使いたいためだけのラッパー？として使っている。ここではKVStoreAというDSLを、Scalaのモナドにしたい意図
def impureCompiler: KVStoreA ~> Id = new (KVStoreA ~> Id) {
  val kvs = mutable.Map.empty[String, Any]

  // コンパイルとは、定義したDSLをScalaでどのように実行するかを定義したものと覚えるとよさそう
  def apply[A](fa: KVStoreA[A]): Id[A] =
    fa match {
      case Put(key, value) =>
        println(s"put($key, $value)")
        kvs(key) = value
        ()
      case Get(key) =>
        println(s"get($key)")
        kvs.get(key).asInstanceOf[A]
      case Delete(key) =>
        println(s"delete($key)")
        kvs.remove(key)
        ()
    }
}

// 3.で定義したprogramは再起構造として表現できるのでfoldMapで折り畳める
// 各DSLの操作を消費する(消費はDSLを読み込むみたいな意だと思う)
// impureCompilerを使用して、操作を有効な言語にコンパイル（Effectがあれば適用(実際に状態が変化するしprintもするぞ)）
// 次のDSLの操作を消費する
// はPure状態(最後の行のこと？)に達するまで再帰的に続け、それを返す
object FreeMonadSample {
  val result: Option[Int] = program.foldMap(impureCompiler)
  // put(wild-cats, 2)
  // get(wild-cats)
  // put(wild-cats, 14)
  // put(tame-cats, 5)
  // get(wild-cats)
  // delete(tame-cats)
  // result: Option[Int] = Some(value = 14)
}

// Stateをつかって上を純粋にできる(まだ実行しない状態にできる)
import cats.data.State

type KVStoreState[A] = State[Map[String, Any], A]
val pureCompiler: KVStoreA ~> KVStoreState = new (KVStoreA ~> KVStoreState) {
  def apply[A](fa: KVStoreA[A]): KVStoreState[A] =
    fa match {
      case Put(key, value) => State.modify(_.updated(key, value))
      case Get(key) =>
        State.inspect(_.get(key).asInstanceOf[A])
      case Delete(key) => State.modify(_ - key)
    }
}

object FreeMonadSample2 {
  // runしたときに実際に動作するってわけ
  val result: (Map[String, Any], Option[Int]) = program.foldMap(pureCompiler).run(Map.empty).value
  // result: (Map[String, Any], Option[Int]) = (
  //   Map("wild-cats" -> 14),
  //   Some(value = 14)
  // )
}
