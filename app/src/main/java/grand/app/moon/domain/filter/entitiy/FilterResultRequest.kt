package grand.app.moon.domain.filter.entitiy

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

class FilterResultRequest(
  @SerializedName("search")
  var search: String = "",
  @SerializedName("category_id")
  var categoryId: Int? = null,
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
  @SerializedName("min_price")
  var min_price: String? = "",
  @SerializedName("max_price")
  var max_price: String? = "",
  @SerializedName("min_rate")
  var min_rate: Int? = null,
  @SerializedName("max_rate")
  var max_rate: Int? = null,
  @SerializedName("properties")
  var properties: ArrayList<FilterProperty>? = arrayListOf(),
  @SerializedName("city_ids")
  var cityIds: ArrayList<Int>? = arrayListOf(),
  @SerializedName("area_ids")
  var areaIds: ArrayList<Int>? = arrayListOf(),
) : Serializable {
}