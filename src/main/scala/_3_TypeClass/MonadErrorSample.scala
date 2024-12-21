package _3_TypeClass

import cats.ApplicativeError

object MonadErrorSample {
  // 最寄りの都市を座標から取得するサンプル。結果はハードコーディング
  def getCityClosestToCoordinate[F[_]](x: (Int, Int))(implicit ae: ApplicativeError[F, String]): F[String] = {
    ae.pure("Minneapolis, MN")
  }

  // 都市から気温を取得するサンプル。結果はハードコーディング
  def getTemperatureByCity[F[_]](city: String)(implicit ae: ApplicativeError[F, String]): F[Int] = {
    ae.pure(78)
  }

  // ↑２つをflatmapで使う処理。型パラメータ部分はF[_]の型をMonadError[*[_], String]であると指定する意味
  // MonadError[*[_], String]の*[_]は使いたいエラー型が入る。例えばEither[String, *]
  // 成功する場合はMonadError[Int]が返る

  // という理解で合ってると思うが、MonadErrorは型パラメータを取らないというエラーがでてしまう
  // def getTemperatureByCoordinates[F[_] : MonadError[*[_], String]](x: (Int, Int)): F[Int] = {
  //   for {
  //     c <- getCityClosestToCoordinate[F](x)
  //     t <- getTemperatureByCity[F](c)
  //   } yield t
  // }
  //
  // type MyEither[A] = Either[String, A] // これがMonadError[*[_], String]の*[_]に入る
  // private val value: MyEither[Int] = getTemperatureByCoordinates[MyEither]((44, 93))
}
