package grand.app.moon.domain.explore.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate

class ExploreListPaginateData(
  @SerializedName("data")
  val list: ArrayList<Explore> = arrayListOf(), meta: Meta = Meta(), links: Links=Links()
) : Paginate(meta, links)