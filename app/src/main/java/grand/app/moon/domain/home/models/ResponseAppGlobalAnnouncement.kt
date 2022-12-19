package grand.app.moon.domain.home.models

import androidx.annotation.Keep

@Keep
data class ResponseAppGlobalAnnouncement(
	val title: String?,
	val description: String?,
	val file: String?,
)
