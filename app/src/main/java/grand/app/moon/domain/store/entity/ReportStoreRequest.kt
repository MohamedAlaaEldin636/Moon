package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName

data class ReportStoreRequest(@SerializedName("store_id")
                              var storeId: Int? = null,
                              @SerializedName("advertisement_id")
                              var advertisementId: Int? = null,

                              @SerializedName("reason_id")
                              var reasonId: Int? = -1,
                              @SerializedName("type")
                              var type: Int? = -1) {

}