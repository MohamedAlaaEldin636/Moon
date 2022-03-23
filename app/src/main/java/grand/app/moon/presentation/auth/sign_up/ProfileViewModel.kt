package grand.app.moon.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(userUseCase: UserLocalUseCase) : BaseViewModel() {

//  var request = ProfileRequest()
//
//  val responseUser =
//    MutableStateFlow<Resource<BaseResponse<User>>>(Resource.Default)
//
//  init {
//    request.isLogin = checkLoggedInUserUseCase.invoke()
//    if(request.isLogin){
//      request.name = userUseCase.invoke().collect {  }
//      request.email = userUseCase.accountRepository.getUserLocal().email
//    }
//  }
//
//  private val TAG = "SignUpViewModel"
//  fun signup(){
//    viewModelScope.launch(job){
//      Log.d(TAG, "signup: "+request.password)
//      useCase.submit(request)
//        .catch { exception -> validationException.value = exception.message?.toInt() }
//        .onEach { result ->
//          response.value = result
//        }.launchIn(viewModelScope)
//    }
//  }
//
//  fun profileUpdate(){
//    viewModelScope.launch(job){
//      Log.d(TAG, "signup: "+request.password)
//      useCase.profileUpdate(request)
//        .catch { exception -> validationException.value = exception.message?.toInt() }
//        .onEach { result ->
//          responseUser.value = result
//        }.launchIn(viewModelScope)
//    }
//
//  }
//
//
//
//  fun changePassword(){
//    clickEvent.value = Constants.CHANGE_PASSWORD
//  }
//
//  override fun onCleared() {
//    job.cancel()
//    super.onCleared()
//  }
}