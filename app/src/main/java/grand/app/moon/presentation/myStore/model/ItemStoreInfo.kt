package grand.app.moon.presentation.myStore.model

data class ItemStoreInfo(
	var imageDrawableRes: Int,
	var notComplete: Boolean,
	var nameStringRes: Int,
	var showArrow: Boolean = true
) {

	companion object {
		fun complete(
			imageDrawableRes: Int,
			nameStringRes: Int,
			showArrow: Boolean = true
		) = ItemStoreInfo(imageDrawableRes, false, nameStringRes, showArrow)
	}

}
