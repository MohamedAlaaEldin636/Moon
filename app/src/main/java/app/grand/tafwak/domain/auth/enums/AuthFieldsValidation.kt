package app.grand.tafwak.domain.auth.enums

enum class AuthFieldsValidation(val value: Int) {
  EMPTY_EMAIL(1),
  INVALID_EMAIL(2),
  EMPTY_PASSWORD(3),
  EMPTY_NAME(4),
  EMPTY_PHONE(5),
  EMPTY_CONFIRM_PASSWORD(6),
  PASSWORD_NOT_MATCH(7),
  EMPTY_TERMS(8),
  EMPTY_IMAGE(9),
  EMPTY_NICK_NAME(10),
  EMPTY_CONTENT(11),
}
