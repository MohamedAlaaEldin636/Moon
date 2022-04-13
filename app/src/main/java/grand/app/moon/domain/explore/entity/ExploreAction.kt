package grand.app.moon.domain.explore.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExploreAction(
  @SerializedName("explore_id")
  @Expose
  var exploreId: Int? = 0,
  @SerializedName("type")
  @Expose
  var type: Int? = 0,
) {
}