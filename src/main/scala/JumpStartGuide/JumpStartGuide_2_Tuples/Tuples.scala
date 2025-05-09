package JumpStartGuide.JumpStartGuide_2_Tuples

import cats.implicits.catsSyntaxTuple3Semigroupal

import scala.util.Random

// 非同期処理を実行する際の実行環境を指定。globalはアプリケーション全体で共有されるスレッドプールのことみたい
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Tuples {

  // mapNがcatsのやつ。Futureを非同期に解決する。
  // processAsync3のようにflatMapは、順次処理なので最初にこけたところしかエラーが返らない
  def processAsync: Future[ProcessingResult] = {
    (intFuture, stringFuture, userFuture).mapN { (value, contents, user) =>
      process(value, contents, user)
    }
  }

  // processAsyncを簡潔に書いたVer
  def processAsync2: Future[ProcessingResult] =
    (intFuture, stringFuture, userFuture).mapN(process)

  def processAsync2IntFailure: Future[ProcessingResult] =
    (intFutureFailure, stringFuture, userFuture).mapN(process)

  def intFutureFailure: Future[Int] = {
    Future.failed(new RuntimeException("何かが失敗1"))

    // ↓の書き方にするとテストのfutureInt.value.get部分でNoSuchElementExceptionになる
    // Thread.sleepがなくてもなる。テスト側で待機させようと思ったけどうまくいかなかったので…
//    Future {
//      // Thread.sleep(700) // 0.7秒待機する。下記のstringFutureFailureより後に失敗する
//      throw new RuntimeException("何かが失敗1")
//    }
  }

  def processAsync2IntAndStringFailure: Future[ProcessingResult] =
    (intFutureFailure, stringFutureFailure, userFuture).mapN(process)

  def stringFutureFailure: Future[String] = {
    Future.failed(new RuntimeException("何かが失敗2"))

//    Future {
//      // Thread.sleep(500) // 0.5秒待機する
//      throw new RuntimeException("何かが失敗2")
//    }
  }

  // これはmapNを使っていない。なのでforによってflatMapになるので非同期に動作しない
  def processAsync3: Future[ProcessingResult] = {
    for {
      value    <- intFuture
      contents <- stringFuture
      user     <- userFuture
    } yield process(value, contents, user)
  }

  def intFuture: Future[Int] = {
    Future {
      Random.nextInt(100)
    }
  }

  def stringFuture: Future[String] = {
    Future {
      // 長さ10の英数字からなるランダムな文字列
      Random.alphanumeric.take(10).mkString
    }
  }

  def userFuture: Future[User] = {
    Future {
      new User("John Doe") // 名無しの権兵衛の意
    }
  }

  def process(value: Int, contents: String, user: User): ProcessingResult = {
    new ProcessingResult(value, contents, user.name)
  }

  class ProcessingResult(val value: Int, val contents: String, val userName: String)
}

class User(val name: String)
