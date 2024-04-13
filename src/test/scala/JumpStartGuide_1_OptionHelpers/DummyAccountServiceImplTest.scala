package JumpStartGuide_1_OptionHelpers;

import org.scalatest.concurrent.TimeLimits
import org.scalatest.diagrams.Diagrams
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

class DummyAccountServiceImplTest
    extends AnyFlatSpec
    with Diagrams
    with TimeLimits {

  "getAccountByIdで" should "Future[Option[Account]]が返ってくる" in {
    val service = new DummyAccountServiceImpl()
    val futureAccount: Future[Option[Account]] = service.getAccountById(1)
    val account: Option[Account] = Await.result(futureAccount, 1.second)

    assert(account.contains(service.dummyAccount))
  }

}
