package grand.app.moon.domain.filter.entitiy

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

class FilterResultRequest(
  @SerializedName("category_id")
  var categoryId: Int = -1,
  @SerializedName("sub_category_id")
  var sub_category_id: Int = -1,
  @SerializedName("other_options")
  var other_options: Int = 1,
  @SerializedName("order_by")
  var order_by: Int = 1,
  @SerializedName("min_price")
  var min_price: String? = "",
  @SerializedName("max_price")
  var max_price: String? = "",
  @SerializedName("min_rate")
  var min_rate: Int? = 1,
  @SerializedName("max_rate")
  var max_rate: Int? = 5,
  @SerializedName("properties")
  var properties: ArrayList<FilterProperty>? = arrayListOf(),
  @SerializedName("city_ids")
  var cityIds: ArrayList<Int>? = arrayListOf(),
) : Serializable {
}