package grand.app.moon.presentation.auth.profile

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.isValidEmail
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  val userUseCase: UserLocalUseCase,
  val useCase: LogInUseCase,
  private val logInUseCase: LogInUseCase,

  ) : BaseViewModel() {

  @Bindable
  val request = UpdateProfileRequest()
  var imageUri: Uri? = null

  private val _response =
    MutableStateFlow<Resource<BaseResponse<User>>>(Resource.Default)
  val response = _response
  var loginResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val user = userUseCase.invoke()

  init {
    request.name = when (user.name) {
      null -> ""
      else -> user.name
    }
    request.phone = user.phone.toString()
    request.country_code = user.country_code
    request.email = user.email
    request.imagePath = when (user.image) {
      null -> ""
      else -> user.image
    }
  }


  private val TAG = "ProfileViewModel"
  fun updateProfile(v: View) {

    if (request.email.trim().isNotEmpty()) {
      if (!request.email.isValidEmail()) {
        showError(v.context, v.context.getString(R.string.please_enter_valid_email));
        return
      }
//      return
    }
    if (request.country_code == "+20" && request.phone.startsWith("0")) {
      request.phone = request.phone.substring(1)
    }

    val phoneOld = user.country_code + user.phone
    val phoneNew = request.country_code + request.phone
    if (phoneNew != phoneOld) {
      val loginRequest= LogInRequest()
      loginRequest.country_code = request.country_code
      loginRequest.phone = request.phone
      logInUseCase(loginRequest)
        .onEach { result ->
          Log.d(TAG, "onLogInClicked: HEREREEREE")
          loginResponse.value = result
        }
        .launchIn(viewModelScope)
    } else {
      useCase.updateProfile(request).onEach { result ->
        response.value = result
      }.launchIn(viewModelScope)
    }
  }


}