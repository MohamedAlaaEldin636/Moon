package grand.app.moon.presentation.auth.log_in

import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  val showSocial = ObservableBoolean(true)
  var request = LogInRequest()
  var _logInResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val logInResponse = _logInResponse

  init {

  }


  private  val TAG = "LogInViewModel"
  fun onLogInClicked(v: View) {
    Log.d(TAG, "onLogInClicked: login")
    if (request.phone.trim().isEmpty()) {
      showError(v.context, v.context.getString(R.string.please_enter_your_phone));
      return
    }
    logInUseCase(request)
      .onEach { result ->
        _logInResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun facebook(v: View){
    LoginManager.getInstance().logOut()

    Log.d(TAG, "facebook: ")
    val callbackManager = CallbackManager.Factory.create()

    val loginManager = LoginManager.getInstance()

    loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)
    loginManager.logIn(
      v.findFragment<LogInFragment>().requireActivity(),
      callbackManager,
      listOf("email", "public_profile"/*, "user_friends"*/),
    )

    loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
      override fun onSuccess(result: LoginResult) {
        performSocialLoginWithApi(v.findFragment(), result.accessToken.userId)
      }

      override fun onError(error: FacebookException) {
        showError(v.context,v.context.getString(R.string.something_went_wrong_please_try_again))
      }

      override fun onCancel() {
        showError(v.context,v.context.getString(R.string.something_went_wrong_please_try_again))
      }
    })
  }

  fun performSocialLoginWithApi(fragment: LogInFragment, userId: String) {

  }


  fun google(view: View){
    val fragment = view.findFragment<LogInFragment>()

    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .build()
    val client = GoogleSignIn.getClient(view.context, options)

    //val account = GoogleSignIn.getLastSignedInAccount(view.context)

    fragment.activityResultGoogleSignIn.launch(client.signInIntent)
  }


}