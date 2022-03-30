package grand.app.moon.domain.ads.entity

import com.google.gson.annotations.SerializedName

data class AddFavouriteAdsRequest(@SerializedName("advertisement_id")
                              var advertisementId: Int? = -1) {

}