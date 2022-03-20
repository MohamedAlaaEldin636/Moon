package grand.app.moon.domain.account.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import javax.inject.Inject

class SetFirstTimeUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  suspend operator fun invoke(isFirstTime: Boolean) = accountRepository.setFirstTime(isFirstTime)
}