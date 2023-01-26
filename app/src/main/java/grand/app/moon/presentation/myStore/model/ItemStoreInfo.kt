package grand.app.moon.presentation.myStore.model

data class ItemStoreInfo(
	var imageDrawableRes: Int,
	var notComplete: Boolean,
	var nameStringRes: Int,
) {

	companion object {
		fun complete(imageDrawableRes: Int, nameStringRes: Int) = ItemStoreInfo(imageDrawableRes, false, nameStringRes)
	}

}
