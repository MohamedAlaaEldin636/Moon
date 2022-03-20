package grand.app.moon.domain.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.general.paginate.Links
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate

class HomePaginateData(
  @SerializedName("data")
  val list: ArrayList<HomeData> = arrayListOf(), meta: Meta = Meta(), links: Links = Links()
) : Paginate(meta, links)