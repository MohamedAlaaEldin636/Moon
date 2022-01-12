package com.structure.base_mvvm.domain.account.use_case

import com.structure.base_mvvm.domain.account.repository.AccountRepository
import com.structure.base_mvvm.domain.auth.entity.model.User
import javax.inject.Inject

class UserLocalUseCase @Inject constructor(private val accountRepository: AccountRepository) {
  operator fun invoke(user: User) = accountRepository.saveUserToLocal(user)
  operator fun invoke(): User? = accountRepository.getUserToLocal()
  operator fun invoke(key: String, value: String) =
    accountRepository.saveKeyToLocal(key, value)

  operator fun invoke(key: String): String {
    return accountRepository.getKeyFromLocal(key)
  }
}