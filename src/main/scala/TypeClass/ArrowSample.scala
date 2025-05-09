package TypeClass

import cats.arrow.Arrow
import cats.syntax.all.*

object ArrowSample {

  // F[_, _]はArrowなので
  // fab:F[A, B]は「Aを引数にBを返す」関数ってこと
  // fac:F[A, C]は「Aを引数にCを返す」関数ってこと
  // combine関数はfabとfacを合成した「Aを引数にBとCのタプルを返す」関数を作る
  def combine[F[_, _]: Arrow, A, B, C](fab: F[A, B], fac: F[A, C]): F[A, (B, C)] =
    // Arrow[F].liftで(a: A) => (a, a)をF[A, (A, A)]型(引数を2つにする)の矢印に持ち上げる
    // これは後続で2つの関数に引数を渡すために、引数を2つにしている
    // >>>は左を右に渡す
    // ***は2つのArrowを(並列？)実行し、結果をタプルで返す
    Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)

  // 平均
  val mean: List[Int] => Double =
    combine(
      (_: List[Int]).sum,
      (_: List[Int]).size
    ) >>> { case (x, y) => x.toDouble / y }

  // 分散
  val variance: List[Int] => Double =
    // 分散は平均二乗誤差の平方から平均二乗誤差を引いたものらしいがよくわからんがサンプルでは2つの関数をあつかえれば良いよね
    combine(
      (
        (_: List[Int]).map(x => x * x)
      ) >>> mean,
      mean
    ) >>> { case (x, y) => x - y * y }

  // 平均と分散を両方やる
  val meanAndVar: List[Int] => (Double, Double) = combine(mean, variance)

  meanAndVar(List(1, 2, 3, 4))
  // res0: (Double, Double) = (2.5, 1.25)

  // より自然な実装。平均はいいが分散はわからんのでそもそも数学の授業でやってないなこれ
  val mean2: List[Int] => Double = xs => xs.sum.toDouble / xs.size

  val variance2: List[Int] => Double = xs => mean2(xs.map(x => x * x)) - scala.math.pow(mean2(xs), 2.0)
}
