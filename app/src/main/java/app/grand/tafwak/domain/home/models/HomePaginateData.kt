package app.grand.tafwak.domain.home.models

import com.google.gson.annotations.SerializedName
import app.grand.tafwak.domain.general.paginate.Links
import app.grand.tafwak.domain.general.paginate.Meta
import app.grand.tafwak.domain.general.paginate.Paginate

class HomePaginateData(
  @SerializedName("data")
  val list: ArrayList<HomeData> = arrayListOf(), meta: Meta = Meta(), links: Links = Links()
) : Paginate(meta, links)