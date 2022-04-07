package grand.app.moon.domain.auth.use_case

import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class VerifyAccountUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userLocalUseCase: UserLocalUseCase
) {
  lateinit var baseViewModel: BaseViewModel

  operator fun invoke(request: VerifyAccountRequest): Flow<Resource<BaseResponse<User>>> = flow {
    if (request.code.isNotEmpty()) {
      emit(Resource.Loading)
      val result = authRepository.verifyAccount(request)
      if (result is Resource.Success) {
        baseViewModel.loginUser(result.value.data.id.toString(),result.value.data.name,result.value.data.image) { callBack ->
          if(callBack){
            baseViewModel.viewModelScope.launch {
              userLocalUseCase(result.value.data)
            }
          }
        }
      }
      emit(result)
    }
  }.flowOn(Dispatchers.IO)

  fun sendCode(request: VerifyAccountRequest): Flow<Resource<BaseResponse<*>>> = flow {
    if (request.code.isNotEmpty()) {
      emit(Resource.Loading)
      val result = authRepository.sendCode(request)
      emit(result)
    }
  }.flowOn(Dispatchers.IO)

}