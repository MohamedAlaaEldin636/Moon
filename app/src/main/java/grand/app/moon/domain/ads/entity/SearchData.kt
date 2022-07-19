package grand.app.moon.domain.ads.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import java.io.Serializable

class SearchData(
    @SerializedName("stores")
  val stores: ArrayList<Store> = arrayListOf(),

    @SerializedName("advertisements")
    val ads: AdsListPaginateData = AdsListPaginateData(),
): Serializable