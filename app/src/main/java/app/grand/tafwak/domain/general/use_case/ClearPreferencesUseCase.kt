package app.grand.tafwak.domain.general.use_case

import app.grand.tafwak.domain.account.repository.AccountRepository
import javax.inject.Inject

class ClearPreferencesUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  operator fun invoke() = accountRepository.clearPreferences()
}