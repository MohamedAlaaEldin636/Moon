package grand.app.moon.domain.subCategory.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.intro.entity.AppTutorial
import java.io.Serializable

@Keep
data class SubCategoryResponse(
  @SerializedName("properties")
  @Expose val properties: ArrayList<Property> = arrayListOf(),
  @SerializedName("advertisements")
  @Expose val advertisements: AdsListPaginateData = AdsListPaginateData(),
  @SerializedName("slider")
  @Expose var slider: ArrayList<AppTutorial> = arrayListOf(),

  ) : Serializable