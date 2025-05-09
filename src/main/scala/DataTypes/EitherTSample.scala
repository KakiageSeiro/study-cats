package DataTypes

import cats.data.EitherT

import scala.concurrent.Future
import scala.util.Try

// EitherTはflatMap2回やらなきゃいけないところを、一回でいいぜ君だ
// どうせこのあとflatMapするんでしょ君と思ってもいい

// Eitherを返す関数たち
def parseDouble(s: String): Either[String, Double] =
  Try(s.toDouble).map(Right(_)).getOrElse(Left(s"$s is not a number"))

def divide(a: Double, b: Double): Either[String, Double] =
  Either.cond(b != 0, a / b, "Cannot divide by zero")

// 上がFutureに入っちゃった関数たち
def parseDoubleAsync(s: String): Future[Either[String, Double]] =
  Future.successful(parseDouble(s))
def divideAsync(a: Double, b: Double): Future[Either[String, Double]] =
  Future.successful(divide(a, b))

import scala.concurrent.ExecutionContext.Implicits.global

// Future[Either[]]だとforできれいに書けないよね。のサンプル
object EitherTSample {
  def divisionProgramAsync(inputA: String, inputB: String): Future[Either[String, Double]] =
    parseDoubleAsync(inputA).flatMap { eitherA =>
      parseDoubleAsync(inputB).flatMap { eitherB =>
        (eitherA, eitherB) match {
          case (Right(a), Right(b)) => divideAsync(a, b)
          case (Left(err), _)       => Future.successful(Left(err))
          case (_, Left(err))       => Future.successful(Left(err))
        }
      }
    }

  // EitherTをつかうとスッキリ
  def divisionProgramAsync_EitherT(inputA: String, inputB: String): EitherT[Future, String, Double] =
    for {
      a      <- EitherT(parseDoubleAsync(inputA))
      b      <- EitherT(parseDoubleAsync(inputB))
      result <- EitherT(divideAsync(a, b))
    } yield result
}
