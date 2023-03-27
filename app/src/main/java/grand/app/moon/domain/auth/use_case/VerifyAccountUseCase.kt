package grand.app.moon.domain.auth.use_case

import android.util.Log
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.ICometChat
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.auth.confirmCode.ConfirmCodeFragment
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class VerifyAccountUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userLocalUseCase: UserLocalUseCase
) {
  lateinit var baseViewModel: BaseViewModel

  private  val TAG = "VerifyAccountUseCase"

  operator fun invoke(request: VerifyAccountRequest): Flow<Resource<BaseResponse<User>>> = flow {
    if (request.code.isNotEmpty()) {
			request.type = if (request.type == ConfirmCodeFragment.ACTUAL_TYPE) {
				ConfirmCodeFragment.TYPE_VERIFY
			}else {
				request.type
			}

      emit(Resource.Loading)
      val result = authRepository.verifyAccount(request)
      /*if (result is Resource.Success) {

        userLocalUseCase(result.value.data)

      }*/
      Log.d(TAG, "invoke: ")
      emit(result)

    }
  }.flowOn(Dispatchers.IO)

  fun sendCode(request: VerifyAccountRequest): Flow<Resource<BaseResponse<*>>> = flow {
//    if (request.code.isNotEmpty()) {
      emit(Resource.Loading)
	  request.type = if (request.type == ConfirmCodeFragment.ACTUAL_TYPE) {
		  ConfirmCodeFragment.TYPE_VERIFY
	  }else {
		  request.type
	  }
      val result = authRepository.sendCode(request)
      emit(result)
//    }
  }.flowOn(Dispatchers.IO)

}