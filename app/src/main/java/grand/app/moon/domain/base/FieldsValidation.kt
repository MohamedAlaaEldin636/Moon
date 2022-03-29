package grand.app.moon.domain.base

enum class FieldsValidation(val value: Int) {
  NO_ERROR(0),
  ERROR(1),
  EMPTY_EMAIL(2),
  INVALID_EMAIL(3),
  EMPTY_PASSWORD(4),
  EMPTY_NAME(5),
  EMPTY_PHONE(6),
  EMPTY_CONFIRM_PASSWORD(7),
  PASSWORD_NOT_MATCH(8),
  EMPTY_TERMS(9),
  EMPTY_IMAGE(10),
  EMPTY_NICK_NAME(11),
  EMPTY_CONTENT(12),
}
