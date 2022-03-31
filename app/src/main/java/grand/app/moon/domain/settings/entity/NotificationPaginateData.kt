package grand.app.moon.domain.settings.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import com.structure.base_mvvm.domain.general.paginate.Meta
import com.structure.base_mvvm.domain.general.paginate.Paginate
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.settings.models.NotificationData

class NotificationPaginateData(
  @SerializedName("data")
  val list: ArrayList<NotificationData> = arrayListOf(), meta: Meta= Meta(), links: Links=Links()
) : Paginate(meta, links)