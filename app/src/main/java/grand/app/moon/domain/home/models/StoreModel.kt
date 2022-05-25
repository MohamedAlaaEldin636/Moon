package grand.app.moon.domain.home.models

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class StoreModel(
  var list: ArrayList<Store> = ArrayList(),
  var position: Int = -1
) : Serializable