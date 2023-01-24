package grand.app.moon.domain.shop

import android.net.Uri

sealed interface MAImagesOrVideo {

	data class Images(val images: List<Uri>) : MAImagesOrVideo

	data class Video(val video: Uri) : MAImagesOrVideo

}