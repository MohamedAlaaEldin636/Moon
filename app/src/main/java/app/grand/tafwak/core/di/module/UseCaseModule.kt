package app.grand.tafwak.core.di.module

import com.structure.base_mvvm.domain.account.repository.AccountRepository
import com.structure.base_mvvm.domain.account.use_case.AccountUseCases
import com.structure.base_mvvm.domain.account.use_case.CheckFirstTimeUseCase
import com.structure.base_mvvm.domain.account.use_case.CheckLoggedInUserUseCase
import com.structure.base_mvvm.domain.account.use_case.LogOutUseCase
import com.structure.base_mvvm.domain.account.use_case.UserLocalUseCase
import com.structure.base_mvvm.domain.account.use_case.SendFirebaseTokenUseCase
import com.structure.base_mvvm.domain.account.use_case.SetFirstTimeUseCase
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.auth.use_case.LogInUseCase
import com.structure.base_mvvm.domain.auth.use_case.RegisterUseCase
import com.structure.base_mvvm.domain.auth.use_case.VerifyAccountUseCase
import com.structure.base_mvvm.domain.countries.repository.CountriesRepository
import com.structure.base_mvvm.domain.countries.use_case.CountriesUseCase
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.educational.use_case.EducationalUseCase
import com.structure.base_mvvm.domain.general.use_case.ClearPreferencesUseCase
import com.structure.base_mvvm.domain.general.use_case.GeneralUseCases
import com.structure.base_mvvm.domain.home.repository.HomeRepository
import com.structure.base_mvvm.domain.home.use_case.HomeUseCase
import com.structure.base_mvvm.domain.intro.repository.IntroRepository
import com.structure.base_mvvm.domain.intro.use_case.IntroUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
  fun provideRegisterUseCase(
    authRepository: AuthRepository,
    userLocalUseCase: UserLocalUseCase
  ): RegisterUseCase = RegisterUseCase(authRepository, userLocalUseCase)

  @Provides
  @Singleton
  fun provideVerifyAccountUseCase(
    authRepository: AuthRepository,
    userLocalUseCase: UserLocalUseCase
  ): VerifyAccountUseCase = VerifyAccountUseCase(authRepository, userLocalUseCase)

  @Provides
  @Singleton
  fun provideCountriesUseCase(
    countriesRepository: CountriesRepository
  ): CountriesUseCase = CountriesUseCase(countriesRepository)

  @Provides
  @Singleton
  fun provideEducationalUseCase(
    countriesRepository: EducationalRepository
  ): EducationalUseCase = EducationalUseCase(countriesRepository)

  @Provides
  @Singleton
  fun provideHomeUseCase(
    homeRepository: HomeRepository
  ): HomeUseCase = HomeUseCase(homeRepository)

  @Provides
  @Singleton
  fun provideIntroUseCase(
    introRepository: IntroRepository
  ): IntroUseCase = IntroUseCase(introRepository)

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