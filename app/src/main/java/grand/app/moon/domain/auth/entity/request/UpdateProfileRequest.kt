package grand.app.moon.domain.auth.entity.request

import android.net.Uri
import androidx.annotation.Keep
import grand.app.moon.domain.utils.BaseRequest

@Keep
data class UpdateProfileRequest(
  var name: String = "",
  var email: String = "",
  var phone: String = "",
  var country_code: String = "",
  var imagePath: String = "",
  @Transient
  var uri: Uri? = null,
  @Transient
  var uriCrop: Uri? = null
) : BaseRequest() {

}