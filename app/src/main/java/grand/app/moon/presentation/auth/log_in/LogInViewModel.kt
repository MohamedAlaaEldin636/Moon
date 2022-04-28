package grand.app.moon.presentation.auth.log_in

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginFragment
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.sendFirebaseSMS
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import grand.app.moon.helpers.login.SocialRequest
import kotlin.math.log


@HiltViewModel
class LogInViewModel @Inject constructor(
  private val logInUseCase: LogInUseCase,
  val userUseCase: UserLocalUseCase,
  val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  lateinit var socialRequest: SocialRequest
  lateinit var registerRequest: SocialRequest
  val showSocial = ObservableBoolean(true)
  var request = LogInRequest()
  var _logInResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  var typeRequest = Constants.SOCIAL_TYPE

  init {
    logInUseCase.baseViewModel = this
  }

  fun checkUser(): Boolean {
    val user = userUseCase.invoke()
    return user.phone.isNotEmpty()
  }

  private val TAG = "LogInViewModel"
  fun onLogInClicked(v: View) {
    Log.d(TAG, "onLogInClicked: login")
    if (request.phone.trim().isEmpty()) {
      showError(v.context, v.context.getString(R.string.please_enter_your_phone));
      return
    }
    typeRequest = Constants.LOGIN
    val fragment = v.findFragment<LogInFragment>()
    v.context.sendFirebaseSMS(
      fragment.requireActivity(),
      v,
      request.country_code + request.phone
    ) { verificationId ->
      v.findNavController().navigate(
        LogInFragmentDirections.actionLogInFragmentToFragmentConfirmCode(
          request.country_code, request.phone, Constants.Verify, verificationId
        )
      )
    }
//    logInUseCase(request)
//      .onEach { result ->
//        _logInResponse.value = result
//      }
//      .launchIn(viewModelScope)
  }

  fun facebook(view: View) {
//    LoginManager.getInstance().logOut()
///*val accessToken = AccessToken.getCurrentAccessToken()
//		val isLoggedIn = accessToken != null && !accessToken.isExpired
//		Timber.e("dewjodj isLoggedIn $isLoggedIn")
//
//		if (isLoggedIn) {
//			// so actual log in -> LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//			return
//		}
//		*/
//    // todo keeps getting error of not being published to live on facebook console which requires app review,
//    //  and if need in debug mode u need to add a tester in Roles in console dunno why isa ?!
//    val callbackManager = CallbackManager.Factory.create()
//
//    val loginManager = LoginManager.getInstance()
//
//    loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)
//    loginManager.logIn(
//      view.findFragment<LoginFragment>().requireActivity(),
//      callbackManager,
//      listOf("email", "public_profile"/*, "user_friends"*/),
//    )
//
//    loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//      override fun onSuccess(result: LoginResult) {
//        Toast.makeText(view.context, "HERE", Toast.LENGTH_SHORT).show()
//      }
//
//      override fun onError(error: FacebookException) {
//        Log.d(TAG, "onError: ${error.message}")
//        view.context.showErrorToast(view.context.getString(R.string.something_went_wrong_please_try_again))
//      }
//
//      override fun onCancel() {
//        view.context.showErrorToast(view.context.getString(R.string.something_went_wrong_please_try_again))
//      }
//    })
  }

  fun twitter(v: View) {

  }

  lateinit var googleClient: GoogleSignInClient
  fun google(view: View) {
    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .build()
    googleClient = GoogleSignIn.getClient(view.context, options)

    //val account = GoogleSignIn.getLastSignedInAccount(view.context)
    clickEvent.value = Constants.GOOGLE
  }

  fun setSocialRegister(googleSignResult: SocialRequest) {
    typeRequest = Constants.SOCIAL_TYPE
    this.socialRequest = googleSignResult
    logInUseCase.socialRegister(socialRequest)
      .onEach { result ->
        _logInResponse.value = result
      }
      .launchIn(viewModelScope)
  }


}