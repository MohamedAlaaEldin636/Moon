package grand.app.moon.domain.account.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import javax.inject.Inject

class CheckLoggedInUserUseCase @Inject constructor(private val accountRepository: AccountRepository) {

  suspend operator fun invoke() = accountRepository.getUserToLocal()
}