package Others

import cats.arrow.Profunctor
import cats.instances.function._ // Function1 のインスタンスが入っている

object ProfunctorSample {
  // // この関数に対して、前処理と後処理を作っていくよ
  // val f: Int => String = _.toString
  //
  // // 入力を変換して、新たな関数を作る（Int を受け取る関数を、Double を受け取る関数にする）
  // val f2: Double => String = Profunctor[Function1].lmap(f)(_.toInt)
  //
  // // 出力を変換して、新たな関数を作る（String の結果を、その長さに変換する）
  // val f3: Int => Int = Profunctor[Function1].rmap(f)(_.length)
  //
  // // Profunctor[Int, String, Double, Int].dimap(f)(f2)(f3)
  // // val f4: Double => Int = Profunctor[Function1].dimap(f)(_.toInt)(_.length)


  def f(): Double = {
    // この関数に対して、前処理と後処理を作っていくよ
    val fab: Double => Double = x => x + 0.3
    // 前処理を作る
    // これによって fab は Double を受け取るが、Int を受け取る関数にできる
    val f: Int => Double = x => x.toDouble / 2
    // 後処理を作る
    // これによって fab は Double を出力するが、それをさらに3倍する
    val g: Double => Double = x => x * 3
    // 前処理と後処理を含めてfabをやる関数
    val h = Profunctor[Function1].dimap(fab)(f)(g)
    h(3)
    // res0: Double= 5.4
  }
}