package grand.app.moon.domain.general.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import javax.inject.Inject

class ClearPreferencesUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  operator fun invoke() = accountRepository.clearPreferences()
}