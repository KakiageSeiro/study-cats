package DataTypes

import DataTypes.FormValidator.RegistrationData
import DataTypes.FormValidatorNec.{ValidationResult, validateForm}
import cats.data.Chain
import cats.data.Validated.{Invalid, Valid}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ValidatedSampleTest extends AnyFlatSpec with Matchers {
  it should "valid" in {
    val actual: ValidationResult[RegistrationData] = validateForm(
      username  = "Joe",
      password  = "Passw0r$1234",
      firstName = "John",
      lastName  = "Doe",
      age       = 21
    )
    assert(actual == Valid(RegistrationData("Joe", "Passw0r$1234", "John", "Doe", 21)))
  }

  it should "invalid" in {
    val actual: ValidationResult[RegistrationData] = validateForm(
      username  = "Joe%%%",
      password  = "password",
      firstName = "John",
      lastName  = "Doe",
      age       = 21
    )
    assert(actual == Invalid(Chain(UsernameHasSpecialCharacters, PasswordDoesNotMeetCriteria)))
  }

}
