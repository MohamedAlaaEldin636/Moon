package grand.app.moon.core.di.module

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.AccountUseCases
import grand.app.moon.domain.account.use_case.CheckFirstTimeUseCase
import grand.app.moon.domain.account.use_case.CheckLoggedInUserUseCase
import grand.app.moon.domain.account.use_case.LogOutUseCase
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.account.use_case.SendFirebaseTokenUseCase
import grand.app.moon.domain.account.use_case.SetFirstTimeUseCase
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.auth.use_case.VerifyAccountUseCase
import grand.app.moon.domain.general.use_case.ClearPreferencesUseCase
import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.intro.repository.IntroRepository
import grand.app.moon.domain.intro.use_case.IntroUseCase
import grand.app.moon.domain.settings.repository.SettingsRepository
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import grand.app.moon.domain.countries.repository.CountriesRepository
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

  @Provides
  @Singleton
  fun provideLogInUseCase(
    authRepository: AuthRepository,
    userLocalUseCase: UserLocalUseCase
  ): LogInUseCase = LogInUseCase(authRepository, userLocalUseCase)

  @Provides
  @Singleton
  fun provideCountriesUseCase(
    countriesRepository: CountriesRepository
  ): CountriesUseCase = CountriesUseCase(countriesRepository)



  @Provides
  @Singleton
  fun provideVerifyAccountUseCase(
    authRepository: AuthRepository,
    userLocalUseCase: UserLocalUseCase
  ): VerifyAccountUseCase = VerifyAccountUseCase(authRepository, userLocalUseCase)


  @Provides
  @Singleton
  fun provideIntroUseCase(
    introRepository: IntroRepository
  ): IntroUseCase = IntroUseCase(introRepository)

  @Provides
  @Singleton
  fun provideSettingsUseCase(
    settingsRepository: SettingsRepository
  ): SettingsUseCase = SettingsUseCase(settingsRepository)

  //public use cases
  @Provides
  @Singleton
  fun provideCheckFirstTimeUseCase(
    accountRepository: AccountRepository
  ): CheckFirstTimeUseCase = CheckFirstTimeUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideCheckLoggedInUserUseCase(
    accountRepository: AccountRepository
  ): CheckLoggedInUserUseCase = CheckLoggedInUserUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideSetFirstTimeUseCase(
    accountRepository: AccountRepository
  ): SetFirstTimeUseCase = SetFirstTimeUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideGeneralUseCases(
    checkFirstTimeUseCase: CheckFirstTimeUseCase,
    checkLoggedInUserUseCase: CheckLoggedInUserUseCase,
    setFirstTimeUseCase: SetFirstTimeUseCase,
    clearPreferencesUseCase: ClearPreferencesUseCase
  ): GeneralUseCases =
    GeneralUseCases(
      checkFirstTimeUseCase,
      checkLoggedInUserUseCase,
      setFirstTimeUseCase,
      clearPreferencesUseCase
    )

  @Provides
  @Singleton
  fun provideLogOutUseCase(
    accountRepository: AccountRepository
  ): LogOutUseCase = LogOutUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideSendFirebaseTokenUseCase(
    accountRepository: AccountRepository
  ): SendFirebaseTokenUseCase = SendFirebaseTokenUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideSaveUserToLocalUseCase(
    accountRepository: AccountRepository
  ): UserLocalUseCase = UserLocalUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideClearPreferencesUseCase(
    accountRepository: AccountRepository
  ): ClearPreferencesUseCase = ClearPreferencesUseCase(accountRepository)

  @Provides
  @Singleton
  fun provideAccountUseCases(
    logOutUseCase: LogOutUseCase,
    sendFirebaseTokenUseCase: SendFirebaseTokenUseCase
  ): AccountUseCases = AccountUseCases(logOutUseCase, sendFirebaseTokenUseCase)
}