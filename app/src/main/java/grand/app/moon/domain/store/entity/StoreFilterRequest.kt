package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.filter.entitiy.FilterProperty
import java.io.Serializable

data class StoreFilterRequest(@SerializedName("search")
                              var search: String = "",
                              @SerializedName("category_id")
                              var categoryId: ArrayList<Int> = arrayListOf(),
                              @SerializedName("page")
                              var page: Int = 0,
                              @Transient
                              var categoryName: String? = null,
                              @SerializedName("sub_category_id")
                              var sub_category_id: Int? = null,
                              @Transient
                              var subCategoryName: String? = null,
                              @SerializedName("store_id")
                              var store_id: Int? = null,
                              @SerializedName("other_options")
                              var other_options: Int = 1,
                              @SerializedName("order_by")
                              var order_by: Int = 1,
                              @SerializedName("min_rate")
                              var min_rate: Int? = null,
                              @SerializedName("max_rate")
                              var max_rate: Int? = null,
                              @SerializedName("properties")
                              var properties: ArrayList<FilterProperty>? = arrayListOf(),
                              @SerializedName("city_ids")
                              var cityIds: ArrayList<Int>? = arrayListOf(),
                              @SerializedName("area_ids")
                              var areaIds: ArrayList<Int>? = arrayListOf(),) : Serializable{

}