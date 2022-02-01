package app.grand.tafwak.core.di.module

import app.grand.tafwak.domain.account.repository.AccountRepository
import app.grand.tafwak.domain.account.use_case.AccountUseCases
import app.grand.tafwak.domain.account.use_case.CheckFirstTimeUseCase
import app.grand.tafwak.domain.account.use_case.CheckLoggedInUserUseCase
import app.grand.tafwak.domain.account.use_case.LogOutUseCase
import app.grand.tafwak.domain.account.use_case.UserLocalUseCase
import app.grand.tafwak.domain.account.use_case.SendFirebaseTokenUseCase
import app.grand.tafwak.domain.account.use_case.SetFirstTimeUseCase
import app.grand.tafwak.domain.auth.repository.AuthRepository
import app.grand.tafwak.domain.auth.use_case.ChangePasswordUseCase
import app.grand.tafwak.domain.auth.use_case.LogInUseCase
import app.grand.tafwak.domain.auth.use_case.RegisterUseCase
import app.grand.tafwak.domain.auth.use_case.VerifyAccountUseCase
import app.grand.tafwak.domain.countries.repository.CountriesRepository
import app.grand.tafwak.domain.countries.use_case.CountriesUseCase
import app.grand.tafwak.domain.educational.repository.EducationalRepository
import app.grand.tafwak.domain.educational.use_case.EducationalUseCase
import app.grand.tafwak.domain.general.use_case.ClearPreferencesUseCase
import app.grand.tafwak.domain.general.use_case.GeneralUseCases
import app.grand.tafwak.domain.groups.repository.GroupDetailsRepository
import app.grand.tafwak.domain.groups.use_case.GroupDetailsUseCase
import app.grand.tafwak.domain.home.repository.HomeRepository
import app.grand.tafwak.domain.home.repository.local.HomeLocalRepository
import app.grand.tafwak.domain.home.use_case.HomeUseCase
import app.grand.tafwak.domain.intro.repository.IntroRepository
import app.grand.tafwak.domain.intro.use_case.IntroUseCase
import app.grand.tafwak.domain.reviews.repository.ReviewsRepository
import app.grand.tafwak.domain.reviews.use_case.ReviewsUseCase
import app.grand.tafwak.domain.settings.repository.SettingsRepository
import app.grand.tafwak.domain.settings.use_case.SettingsUseCase
import app.grand.tafwak.domain.student_teacher.repository.StudentTeacherRepository
import app.grand.tafwak.domain.student_teacher.use_case.StudentTeacherUseCase
import app.grand.tafwak.domain.teacher_profile.repository.TeacherProfileRepository
import app.grand.tafwak.domain.teacher_profile.use_case.TeacherProfileUseCase
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
  fun provideChangePasswordUseCase(
    authRepository: AuthRepository,
  ): ChangePasswordUseCase = ChangePasswordUseCase(authRepository)

  @Provides
  @Singleton
  fun provideCountriesUseCase(
    countriesRepository: CountriesRepository,
    userLocalUseCase: UserLocalUseCase
  ): CountriesUseCase = CountriesUseCase(countriesRepository, userLocalUseCase)

  @Provides
  @Singleton
  fun provideEducationalUseCase(
    countriesRepository: EducationalRepository,
    userLocalUseCase: UserLocalUseCase
  ): EducationalUseCase = EducationalUseCase(countriesRepository, userLocalUseCase)

  @Provides
  @Singleton
  fun provideHomeUseCase(
    homeRepository: HomeRepository,
    homeLocalRepository: HomeLocalRepository
  ): HomeUseCase = HomeUseCase(homeRepository, homeLocalRepository)

  @Provides
  @Singleton
  fun provideStudentTeacherUseCase(
    studentTeacherRepository: StudentTeacherRepository
  ): StudentTeacherUseCase = StudentTeacherUseCase(studentTeacherRepository)

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

  @Provides
  @Singleton
  fun provideTeacherProfileUseCase(
    teacherRepository: TeacherProfileRepository
  ): TeacherProfileUseCase = TeacherProfileUseCase(teacherRepository)

  @Provides
  @Singleton
  fun provideReviewsUseCase(
    reviewsRepository: ReviewsRepository
  ): ReviewsUseCase = ReviewsUseCase(reviewsRepository)

  @Provides
  @Singleton
  fun provideGroupDetailsUseCase(
    groupDetailsRepository: GroupDetailsRepository
  ): GroupDetailsUseCase = GroupDetailsUseCase(groupDetailsRepository)

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