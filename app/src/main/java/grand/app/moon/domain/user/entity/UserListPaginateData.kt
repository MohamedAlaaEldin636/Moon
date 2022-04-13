package grand.app.moon.domain.user.entity

import com.google.gson.annotations.SerializedName
import com.structure.base_mvvm.domain.general.paginate.Links
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.general.paginate.Meta
import grand.app.moon.domain.general.paginate.Paginate
import grand.app.moon.domain.settings.models.NotificationData

class UserListPaginateData(
    @SerializedName("data")
  val list: ArrayList<User> = arrayListOf(), meta: Meta = Meta(), links: Links=Links()
) : Paginate(meta, links)