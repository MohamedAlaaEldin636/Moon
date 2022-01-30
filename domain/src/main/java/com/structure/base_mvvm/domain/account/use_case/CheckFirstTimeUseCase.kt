package com.structure.base_mvvm.domain.account.use_case

import com.structure.base_mvvm.domain.account.repository.AccountRepository
import javax.inject.Inject

class CheckFirstTimeUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  suspend operator fun invoke() = accountRepository.isFirstTime()
}