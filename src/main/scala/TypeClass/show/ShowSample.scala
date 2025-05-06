package TypeClass.show

import cats.*
import cats.syntax.all.*


object ShowSample {
  case class Person(name: String, age: Int)
  
  // Person型のShowインスタンスを定義
  implicit val personShow: Show[Person] = Show.show { person =>
    s"${person.name}さんは${person.age}歳です"
  }

  def showToString : String = {
    val person = Person("Alice", 30)
    person.toString // "Person(Alice,30)"
  }
  
  def showSample : String = {
    val person = Person("Alice", 30)
    person.show // "Aliceさんは30歳です"  
  }
}