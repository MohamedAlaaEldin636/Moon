package grand.app.moon.presentation.myAds.model

import android.net.Uri

sealed interface LocalOrApiItemImage {

	data class Local(val uri: Uri) : LocalOrApiItemImage

	data class Api(val itemImage: ItemImage) : LocalOrApiItemImage

}