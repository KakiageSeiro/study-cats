package TypeClass

import org.scalatest.flatspec.AnyFlatSpec

class AlternativeSampleTest extends AnyFlatSpec {

  behavior.of("AlternativeSampleTest")

  it should "empty" in {
    val actual: Seq[Int] = Vector()
    assert(actual === AlternativeSample.empty)
  }

  it should "pureOfFive" in {
    val actual: Seq[Int] = Vector(5)
    assert(actual === AlternativeSample.pureOfFive)
  }

  it should "concatenated" in {
    val actual: Seq[Int] = Vector(7, 8)
    assert(actual === AlternativeSample.concatenated)
  }

  it should "concatenated2" in {
    val actual: Seq[Int] = Vector(7, 8)
    assert(actual === AlternativeSample.concatenated2)
  }

  it should "apForVectors" in {
    val actual: Seq[Int] = Vector(14, 16, 12, 13)
    assert(actual === AlternativeSample.apForVectors)
  }

  behavior.of("Alternativeを実装したデコーダーTest")

  it should "555" in {
    val actual = Right(555)
    assert(actual === Alternativeを実装したDecoderを使ってみる.decoder.decode("555"))
  }

  it should "5a" in {
    val actual = Right(10)
    assert(actual === Alternativeを実装したDecoderを使ってみる.decoder.decode("5a"))
  }

  behavior.of("Alternativeでリクエストの結果をパーティショニングtest")

  it should "Vector[(Int, String)]とVector[(Int, Long)]のタプルを分類" in {
    val actual =
      (
        // リクエスト失敗したやつら
        Vector(
          (6, "Server error"),
          (99, "Server error"),
          (1200, "Bad request"),
          (8, "Bad request")
        ),
        // リクエスト成功したやつら
        Vector((5, 200L), (7, 200L), (22, 200L))
      )

    assert(actual === Alternativeでリクエストの結果をパーティショニング.partitionedResults)
  }

}
