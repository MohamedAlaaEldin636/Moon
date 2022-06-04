package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName

data class ReportAdsRequest(
                            @SerializedName("advertisement_id")
                              var advertisementId: Int? = null,

                            @SerializedName("reason_id")
                              var reasonId: Int? = -1) {

}