package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName

data class FollowStoreRequest(@SerializedName("store_id")
                              var storeId: Int? = -1) {

}