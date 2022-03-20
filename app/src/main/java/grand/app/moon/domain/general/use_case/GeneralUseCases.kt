package grand.app.moon.domain.general.use_case

import grand.app.moon.domain.account.use_case.CheckFirstTimeUseCase
import grand.app.moon.domain.account.use_case.CheckLoggedInUserUseCase
import grand.app.moon.domain.account.use_case.SetFirstTimeUseCase

class GeneralUseCases(
  val checkFirstTimeUseCase: CheckFirstTimeUseCase,
  val checkLoggedInUserUseCase: CheckLoggedInUserUseCase,
  val setFirstTimeUseCase: SetFirstTimeUseCase,
  val clearPreferencesUseCase: ClearPreferencesUseCase
)