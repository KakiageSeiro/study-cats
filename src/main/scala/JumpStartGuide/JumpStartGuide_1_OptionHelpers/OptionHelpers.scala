package JumpStartGuide.JumpStartGuide_1_OptionHelpers

import JumpStartGuide.JumpStartGuide_1_OptionHelpers.OptionHelpers.ToFutureSuccessful

import scala.concurrent.Future

// https://typelevel.org/cats/jump_start_guide.html には以下のように書かれている
// `Future(Some(obj))ではなく.some.asFutureのように書くことで、戻り値がラッパーの型ではなく実際の値(objのこと？)に焦点があたるので読みやすい`
// が、対して変わらないように見える。もっとネストしたときに良いのだろうか？
// `テストのためにサービス・メソッドのダミー実装を提供する必要がある場合に、可読性が向上することがあります`
// とも書かれているが、これもよくわからない
object OptionHelpers {
  implicit class ToFutureSuccessful[T](obj: T) {
    def asFuture: Future[T] = Future.successful(obj)
  }
}

case class Account(name: Option[String]) {
  def some(): Option[Account] = name match {
    case Some(value) => Some(Account(name))
    case None        => None
  }
}

trait AccountService {
  def getAccountById(id: Int): Future[Option[Account]]
}

class DummyAccountServiceImpl extends AccountService {

  def dummyAccount: Account = Account(Some("テストアカウント"))

  override def getAccountById(id: Int): Future[Option[Account]] = dummyAccount.some().asFuture
}
