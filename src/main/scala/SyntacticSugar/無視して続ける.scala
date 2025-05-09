package SyntacticSugar

import cats.effect.IO

// F[A] 型の値をシーケンスする
// 左側の結果を無視して右側の結果を取る (>>)
// 一瞬なんのためにあるの？となるが以下のように使う
//   ログを出しておいてその後の処理は普通に戻り値を使う
//   リソースの初期化をしたあとに、リソースを利用する処理(DBコネクションとかね)
object 無視して続ける {
  def 左は無視(): Int = {
    val log: IO[Unit]        = IO(println("ログだしたよ..."))
    val computation: IO[Int] = IO.pure(111)

    // ログを出力してから計算を実行し、計算の結果だけを取る
    val ログだして計算結果とる: IO[Int] = log >> computation

    import cats.effect.unsafe.implicits.global
    ログだして計算結果とる.unsafeRunSync()
  }
}
