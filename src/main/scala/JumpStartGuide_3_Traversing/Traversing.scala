package JumpStartGuide_3_Traversing

import cats.implicits.catsSyntaxTuple3Semigroupal
import cats.syntax.all._

import scala.util.Random

// 非同期処理を実行する際の実行環境を指定。globalはアプリケーション全体で共有されるスレッドプールのことみたい
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Traversing {
  def updateUser(user: User): Future[User] = {
    Future {
      // 長さ10の英数字からなるランダムな文字列
      User(Random.alphanumeric.take(10).mkString)
    }
  }

  def updateUsers(users: List[User]): Future[List[User]] = {
    users.traverse(updateUser)
  }
}

case class User(name: String){}