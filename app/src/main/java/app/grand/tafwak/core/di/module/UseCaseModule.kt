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
import com.structure.base_mvvm.domain.auth.use_case.ChangePasswordUseCase
import com.structure.base_mvvm.domain.auth.use_case.LogInUseCase
import com.structure.base_mvvm.domain.auth.use_case.RegisterUseCase
import com.structure.base_mvvm.domain.auth.use_case.VerifyAccountUseCase
import com.structure.base_mvvm.domain.countries.repository.CountriesRepository
import com.structure.base_mvvm.domain.countries.use_case.CountriesUseCase
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.educational.use_case.EducationalUseCase
import com.structure.base_mvvm.domain.general.use_case.ClearPreferencesUseCase
import com.structure.base_mvvm.domain.general.use_case.GeneralUseCases
import com.structure.base_mvvm.domain.groups.repository.GroupDetailsRepository
import com.structure.base_mvvm.domain.groups.use_case.GroupDetailsUseCase
import com.structure.base_mvvm.domain.home.repository.HomeRepository
import com.structure.base_mvvm.domain.home.use_case.HomeUseCase
import com.structure.base_mvvm.domain.intro.repository.IntroRepository
import com.structure.base_mvvm.domain.intro.use_case.IntroUseCase
import com.structure.base_mvvm.domain.reviews.repository.ReviewsRepository
import com.structure.base_mvvm.domain.reviews.use_case.ReviewsUseCase
import com.structure.base_mvvm.domain.settings.repository.SettingsRepository
import com.structure.base_mvvm.domain.settings.use_case.SettingsUseCase
import com.structure.base_mvvm.domain.student_teacher.repository.StudentTeacherRepository
import com.structure.base_mvvm.domain.student_teacher.use_case.StudentTeacherUseCase
import com.structure.base_mvvm.domain.teacher_profile.repository.TeacherProfileRepository
import com.structure.base_mvvm.domain.teacher_profile.use_case.TeacherProfileUseCase
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
    homeRepository: HomeRepository
  ): HomeUseCase = HomeUseCase(homeRepository)

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