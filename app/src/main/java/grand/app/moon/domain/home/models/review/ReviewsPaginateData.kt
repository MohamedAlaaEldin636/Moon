package grand.app.moon.domain.home.models.review

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate
import grand.app.moon.domain.settings.models.NotificationData

class ReviewsPaginateData(
    @SerializedName("data")
  val list: ArrayList<Reviews> = arrayListOf(), meta: Meta = Meta(), links: Links=Links()
) : Paginate(meta, links)