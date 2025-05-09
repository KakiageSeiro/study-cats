package DataTypes

import cats.data.Const
import cats.syntax.all.*
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FreeApplicativeSampleTest extends AnyFlatSpec with ScalaFutures with Matchers {

  "作ったvalidator" should "が動く" in {
    assert(!FreeApplicativeSample.validator("1234"))
    assert(FreeApplicativeSample.validator("12345"))
  }

  "Log" should "が動く" in {
    assert(FreeApplicativeSample_Log.logValidation_prog() == List("size >= 5", "has number"))
    assert(
      FreeApplicativeSample_Log.logValidation_size_hasNumber_size() == List("size >= 5", "has number", "size >= 10")
    )
    assert(FreeApplicativeSample_Log.logValidation_map2() == List("has number", "size >= 3"))
  }

  // "並列とLogセットの解釈器" should "が動く" in {
  //   // 入力 "1234" は長さが 4 なので Size(5) には引っかかり、かつ "1234" は数字を含むので HasNumber は true
  //   // prog は両方の結果を && で結合しているため、結果は false となるはずです
  //   val (resultInvalid, logsInvalid) = runValidateAndLog(FreeApplicativeSample_Par_Log.prodValidation)("1234")
  //   resultInvalid shouldBe false
  //   // ログは prog 内での操作順に従い、"size >= 5" と "has number" が蓄積されるはず
  //   logsInvalid shouldEqual List("size >= 5", "has number")
  //
  //   // 入力 "12345" は長さ 5 なので Size(5) をパスし、かつ数字を含むので HasNumber も true
  //   // したがって結果は true になるはずです
  //   val (resultValid, logsValid) = runValidateAndLog(FreeApplicativeSample_Par_Log.prodValidation)("12345")
  //   resultValid shouldBe true
  //   logsValid shouldEqual List("size >= 5", "has number")
  // }

  "並列とログを両方やる" should "バリデーションとログ集めを両方やれている" in {
    // ValidateAndLog[A]はTuple2K[ParValidator, Log, A] なので、まずはそれを実行するヘルパーを定義
    def runValidateAndLog[A](
      valAndLog: FreeApplicativeSample_Par_Log.ValidateAndLog[A]
    )(input: String): Future[(A, List[String])] = {
      implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

      val validator: ParValidator[A] = valAndLog.first
      val log: Log[A]                = valAndLog.second

      // Kleisli[Future, String, A] は String => Future[A] のラッパー
      // validatorはKleisli[Future, String, A]なので、run(input)でFuture[A]を取れる
      val resultF: Future[A] = validator.run(input)

      // Log[A] = Const[List[String], A]
      // getConstで内部のList[String]を取れる
      val logs: List[String] = log.getConst
      resultF.map(result => (result, logs))
    }

    val prodVal = FreeApplicativeSample_Par_Log.prodValidation

    // "1234"では、Size(5)は false(文字列長 4 < 5)なので、結果はfalseになる
    val (result1, logs1) = runValidateAndLog(prodVal)("1234").futureValue
    assert(!result1)
    assert(logs1 == List("size >= 5", "has number"))

    // "12345"では、検証条件を両方ともパスするので結果はtrue
    val (result2, logs2) = runValidateAndLog(prodVal)("12345").futureValue
    assert(result2)
    assert(logs2 == List("size >= 5", "has number"))
  }
}
