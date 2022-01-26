package com.structure.base_mvvm.domain.account.use_case

import com.structure.base_mvvm.domain.account.repository.AccountRepository
import com.structure.base_mvvm.domain.auth.entity.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserLocalUseCase @Inject constructor(private val accountRepository: AccountRepository) {
  operator fun invoke(user: User) = accountRepository.saveUserToLocal(user)
  operator fun invoke(): User? = accountRepository.getUserToLocal()
  operator fun invoke(key: String, value: String) =
    accountRepository.saveKeyToLocal(key, value)

  operator fun invoke(key: String): String {
    return accountRepository.getKeyFromLocal(key)
  }

  fun saveToken(value: String) {
    flow {
      emit(accountRepository.saveFirebaseTokenToLocal(value))
    }
  }

  fun getToken(): Flow<String> =accountRepository.getFirebaseTokenToLocal()
//    flow {
//      emit(accountRepository.getFirebaseTokenToLocal())
//    }.flowOn(Dispatchers.IO)

}