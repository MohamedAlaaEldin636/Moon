package app.grand.tafwak.core.di.module

import com.structure.base_mvvm.data.account.data_source.remote.AccountRemoteDataSource
import com.structure.base_mvvm.data.account.repository.AccountRepositoryImpl
import com.structure.base_mvvm.data.auth.data_source.remote.AuthRemoteDataSource
import com.structure.base_mvvm.data.auth.repository.AuthRepositoryImpl
import com.structure.base_mvvm.data.countries.data_source.CountriesRemoteDataSource
import com.structure.base_mvvm.data.countries.repository.CountriesRepositoryImpl
import com.structure.base_mvvm.data.educational.data_source.EducationalRemoteDataSource
import com.structure.base_mvvm.data.educational.repository.EducationalRepositoryImpl
import com.structure.base_mvvm.data.general.data_source.remote.GeneralRemoteDataSource
import com.structure.base_mvvm.data.general.repository.GeneralRepositoryImpl
import com.structure.base_mvvm.data.home.data_source.remote.HomeRemoteDataSource
import com.structure.base_mvvm.data.home.repository.HomeRepositoryImpl
import com.structure.base_mvvm.data.intro.data_source.IntroRemoteDataSource
import com.structure.base_mvvm.data.intro.repository.IntroRepositoryImpl
import com.structure.base_mvvm.data.local.preferences.AppPreferences
import com.structure.base_mvvm.data.reviews.data_source.ReviewsRemoteDataSource
import com.structure.base_mvvm.data.reviews.repository.ReviewsRepositoryImpl
import com.structure.base_mvvm.data.settings.data_source.remote.SettingsRemoteDataSource
import com.structure.base_mvvm.data.settings.repository.SettingsRepositoryImpl
import com.structure.base_mvvm.data.studentTeacher.data_source.StudentTeacherRemoteDataSource
import com.structure.base_mvvm.data.studentTeacher.repository.StudentTeacherRepositoryImpl
import com.structure.base_mvvm.data.teacher_profile.data_source.TeacherProfileRemoteDataSource
import com.structure.base_mvvm.data.teacher_profile.repository.TeacherProfileRepositoryImpl
import com.structure.base_mvvm.domain.account.repository.AccountRepository
import com.structure.base_mvvm.domain.auth.repository.AuthRepository
import com.structure.base_mvvm.domain.countries.repository.CountriesRepository
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.general.repository.GeneralRepository
import com.structure.base_mvvm.domain.home.repository.HomeRepository
import com.structure.base_mvvm.domain.intro.repository.IntroRepository
import com.structure.base_mvvm.domain.reviews.repository.ReviewsRepository
import com.structure.base_mvvm.domain.settings.repository.SettingsRepository
import com.structure.base_mvvm.domain.student_teacher.repository.StudentTeacherRepository
import com.structure.base_mvvm.domain.teacher_profile.repository.TeacherProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

  @Provides
  @Singleton
  fun provideGeneralRepository(
    remoteDataSource: GeneralRemoteDataSource,
    appPreferences: AppPreferences
  ): GeneralRepository = GeneralRepositoryImpl(remoteDataSource, appPreferences)

  @Provides
  @Singleton
  fun provideAuthRepository(
    remoteDataSource: AuthRemoteDataSource,
  ): AuthRepository = AuthRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideCountriesRepository(
    remoteDataSource: CountriesRemoteDataSource,
  ): CountriesRepository = CountriesRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideEducationalRepository(
    remoteDataSource: EducationalRemoteDataSource,
  ): EducationalRepository = EducationalRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideAccountRepository(
    remoteDataSource: AccountRemoteDataSource,
    appPreferences: AppPreferences
  ): AccountRepository = AccountRepositoryImpl(remoteDataSource, appPreferences)

  @Provides
  @Singleton
  fun provideSettingsRepository(
    remoteDataSource: SettingsRemoteDataSource
  ): SettingsRepository = SettingsRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideHomeRepository(
    remoteDataSource: HomeRemoteDataSource
  ): HomeRepository = HomeRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideIntroRepository(
    remoteDataSource: IntroRemoteDataSource
  ): IntroRepository = IntroRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideStudentTeacherRepository(
    remoteDataSource: StudentTeacherRemoteDataSource
  ): StudentTeacherRepository = StudentTeacherRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideTeacherProfileRepository(
    remoteDataSource: TeacherProfileRemoteDataSource
  ): TeacherProfileRepository = TeacherProfileRepositoryImpl(remoteDataSource)
 @Provides
  @Singleton
  fun provideReviewsRepository(
    remoteDataSource: ReviewsRemoteDataSource
  ): ReviewsRepository = ReviewsRepositoryImpl(remoteDataSource)

}