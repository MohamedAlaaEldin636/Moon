package grand.app.moon.domain.auth.entity.request

import androidx.annotation.Keep
import grand.app.moon.domain.utils.BaseRequest
import grand.app.moon.presentation.base.utils.Constants

@Keep
open class RegisterRequest(
    var name: String = "",
    var nickname: String = "",
    var account_type: String = Constants.STUDENT_TYPE,
    var register_steps: Int = 1,
    var email: String = "",
    var password: String = "",
    var password_confirmation: String = "",
    var phone: String = "",
    var device_token: String = "",
    var isAccept: Boolean = false
) : BaseRequest()

class RegisterValidationException(private val validationType: String) : Exception(validationType)
