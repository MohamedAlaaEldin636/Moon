package app.grand.tafwak.domain.general.use_case

import app.grand.tafwak.domain.account.use_case.CheckFirstTimeUseCase
import app.grand.tafwak.domain.account.use_case.CheckLoggedInUserUseCase
import app.grand.tafwak.domain.account.use_case.SetFirstTimeUseCase

class GeneralUseCases(
  val checkFirstTimeUseCase: CheckFirstTimeUseCase,
  val checkLoggedInUserUseCase: CheckLoggedInUserUseCase,
  val setFirstTimeUseCase: SetFirstTimeUseCase,
  val clearPreferencesUseCase: ClearPreferencesUseCase
)