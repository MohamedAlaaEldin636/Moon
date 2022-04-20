package grand.app.moon.domain.settings.models

data class NotificationData(
  val created_at: String,
  val text: String,
  val id: Int,
  val logo: String,
  val image: String,
  val notify_type: String
)