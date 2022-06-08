package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreFilterRequest(@SerializedName("page")
                              var page: Int = 0,
                              @SerializedName("order_by")
                              var orderBy: Int = 0,
                              @SerializedName("search")
                              var search: String = "",
                              @SerializedName("category_ids")
                              var category_ids: ArrayList<Int> = arrayListOf(),
                              @SerializedName("city_ids")
                              var city_ids: ArrayList<Int> = arrayListOf()) : Serializable{

}