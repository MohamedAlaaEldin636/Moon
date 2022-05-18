package grand.app.moon.domain.ads.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate
import grand.app.moon.domain.home.models.Advertisement
import java.io.Serializable

class AdsListPaginateData(
    @SerializedName("data")
  val list: ArrayList<Advertisement> = arrayListOf(), meta: Meta = Meta(), links: Links=Links(),

    @SerializedName("total")
    val total: Int = 0,
) : Paginate(meta, links) , Serializable