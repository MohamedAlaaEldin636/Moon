package grand.app.moon.domain.filter.entitiy


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.presentation.filter.FILTER_TYPE
import java.io.Serializable

@Keep
data class FilterProperty(
  @SerializedName("children")
  var children: ArrayList<Children> = arrayListOf(),
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("is_range")
  val isRange: Int = 0,
  @SerializedName("name")
  var name: String = "",
  @SerializedName("text")
  val text: String = "",
  @SerializedName("type")
  var type: Int = 0,
  var selectedText: String = MyApplication.instance.resources.getString(R.string.all),
  var filterType: FILTER_TYPE = FILTER_TYPE.SINGLE_SELECT,
  var selectedList: ArrayList<Int> = arrayListOf()
) : Serializable