package DataTypes

import DataTypes.FormValidator.RegistrationData
import cats.data.{Validated, ValidatedNec}
import cats.implicits.*

// Validatedは、バリデーションしたい条件や項目が複数ある場合に、満たしていないものを"全て"あつめてユーザーに見せる

// まずバリデーションエラーの時用のエラーメッセージを持つものを定義
sealed trait DomainValidation {
  def errorMessage: String
}

case object UsernameHasSpecialCharacters extends DomainValidation {
  def errorMessage: String = "Username cannot contain special characters."
}

case object PasswordDoesNotMeetCriteria extends DomainValidation {
  def errorMessage: String =
    "Password must be at least 10 characters long, including an uppercase and a lowercase letter, one number and one special character."
}

case object FirstNameHasSpecialCharacters extends DomainValidation {
  def errorMessage: String = "First name cannot contain spaces, numbers or special characters."
}

case object LastNameHasSpecialCharacters extends DomainValidation {
  def errorMessage: String = "Last name cannot contain spaces, numbers or special characters."
}

case object AgeIsInvalid extends DomainValidation {
  def errorMessage: String = "You must be aged 18 and not older than 75 to use our services."
}

// 次にバリデーション自体を実行する関数を定義
sealed trait FormValidator {
  def validateUserName(userName: String): Either[DomainValidation, String] =
    Either.cond(
      userName.matches("^[a-zA-Z0-9]+$"),
      userName,
      UsernameHasSpecialCharacters
    )

  def validatePassword(password: String): Either[DomainValidation, String] =
    Either.cond(
      password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"),
      password,
      PasswordDoesNotMeetCriteria
    )

  def validateFirstName(firstName: String): Either[DomainValidation, String] =
    Either.cond(
      firstName.matches("^[a-zA-Z]+$"),
      firstName,
      FirstNameHasSpecialCharacters
    )

  def validateLastName(lastName: String): Either[DomainValidation, String] =
    Either.cond(
      lastName.matches("^[a-zA-Z]+$"),
      lastName,
      LastNameHasSpecialCharacters
    )

  def validateAge(age: Int): Either[DomainValidation, Int] =
    Either.cond(
      age >= 18 && age <= 75,
      age,
      AgeIsInvalid
    )

  final case class RegistrationData(username: String, password: String, firstName: String, lastName: String, age: Int)

  // これはValidatedを使わないバリデーション処理。forはfail-fast(コケたらそこで終わる)なので、Usernameがだめだったら、passwordが良いかどうかはわからない
  def validateForm(
    username:  String,
    password:  String,
    firstName: String,
    lastName:  String,
    age:       Int
  ): Either[DomainValidation, RegistrationData] = {
    for {
      validatedUserName  <- validateUserName(username)
      validatedPassword  <- validatePassword(password)
      validatedFirstName <- validateFirstName(firstName)
      validatedLastName  <- validateLastName(lastName)
      validatedAge       <- validateAge(age)
    } yield RegistrationData(validatedUserName, validatedPassword, validatedFirstName, validatedLastName, validatedAge)
  }

}
object FormValidator extends FormValidator

// Validatedをつかう版はこちら
// .toValidatedでEitherをValidatedにできる
def validateUserName(userName: String): Validated[DomainValidation, String] =
  FormValidator.validateUserName(userName).toValidated
def validatePassword(password: String): Validated[DomainValidation, String] =
  FormValidator.validatePassword(password).toValidated
def validateFirstName(firstName: String): Validated[DomainValidation, String] =
  FormValidator.validateFirstName(firstName).toValidated
def validateLastName(lastName: String): Validated[DomainValidation, String] =
  FormValidator.validateLastName(lastName).toValidated
def validateAge(age: Int): Validated[DomainValidation, Int] = FormValidator.validateAge(age).toValidated

sealed trait FormValidatorNec {
  // ValidatedNecは成功時には値Aを保持し、失敗時にはNon-Empty Chain(NEC)、つまり少なくとも1つのエラーを持つリストとして保持する
  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  private def validateUserName(userName: String): ValidationResult[String] =
    // validNecで成功の、invalidNecで失敗のValidatedNecにリフトする
    if (userName.matches("^[a-zA-Z0-9]+$")) userName.validNec else UsernameHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
    else PasswordDoesNotMeetCriteria.invalidNec

  private def validateFirstName(firstName: String): ValidationResult[String] =
    if (firstName.matches("^[a-zA-Z]+$")) firstName.validNec else FirstNameHasSpecialCharacters.invalidNec

  private def validateLastName(lastName: String): ValidationResult[String] =
    if (lastName.matches("^[a-zA-Z]+$")) lastName.validNec else LastNameHasSpecialCharacters.invalidNec

  private def validateAge(age: Int): ValidationResult[Int] =
    if (age >= 18 && age <= 75) age.validNec else AgeIsInvalid.invalidNec

  def validateForm(
    username:  String,
    password:  String,
    firstName: String,
    lastName:  String,
    age:       Int
  ): ValidationResult[RegistrationData] = {
    (
      validateUserName(username),
      validatePassword(password),
      validateFirstName(firstName),
      validateLastName(lastName),
      validateAge(age)
    ).mapN(RegistrationData.apply)
  }
}

// 利用側でimportするためにobjectにしてるっぽい。そもそもtraitでなくobjectとして定義すれば良いのでは？
// たぶんバリデータをextentsした別のバリデーションが必要かも知れないことの考慮、たとえば管理者権限のユーザーを登録する場合は、さらにフォームの項目が増えるなどの場合を考慮してtraitにしてるかも？
object FormValidatorNec extends FormValidatorNec
