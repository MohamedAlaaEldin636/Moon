package grand.app.moon.presentation.myAds.model

data class LocationData(
    val latitude: String,
    val longitude: String,
    val address: String,
)

fun LocationData?.orEmpty() = this?.copy() ?: LocationData("", "", "")
