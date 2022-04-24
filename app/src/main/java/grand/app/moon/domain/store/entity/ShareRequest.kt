package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName

data class ShareRequest(@SerializedName("advertisement_id")
                              var advertisement: Int? = -1) {

}