package JumpStartGuide_3_Traversing

import cats.syntax.all.*

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

  private val foo: List[Future[String]] = List(Future("hello"), Future("world"))
  def sequence(): Future[List[String]] = {
    foo.sequence
  }
}
case class User(name: String){}