package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate
import grand.app.moon.domain.home.models.Store

class StoreListPaginateData(
  @SerializedName("data")
  val list: ArrayList<Store> = arrayListOf(), meta: Meta = Meta(), links: Links=Links()
) : Paginate(meta, links)