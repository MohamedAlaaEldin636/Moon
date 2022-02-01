package app.grand.tafwak.core.di.module

import app.grand.tafwak.data.account.data_source.remote.AccountServices
import app.grand.tafwak.data.auth.data_source.remote.AuthServices
import app.grand.tafwak.data.countries.data_source.CountriesServices
import app.grand.tafwak.data.educational.data_source.EducationalServices
import app.grand.tafwak.data.general.data_source.remote.GeneralServices
import app.grand.tafwak.data.groupDetails.data_source.GroupDetailsServices
import app.grand.tafwak.data.home.data_source.remote.HomeServices
import app.grand.tafwak.data.intro.data_source.IntroServices
import app.grand.tafwak.data.reviews.data_source.ReviewsServices
import app.grand.tafwak.data.settings.data_source.remote.SettingsServices
import app.grand.tafwak.data.studentTeacher.data_source.StudentTeacherServices
import app.grand.tafwak.data.teacher_profile.data_source.TeacherProfileServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServicesModule {

  @Provides
  @Singleton
  fun provideAuthServices(retrofit: Retrofit): AuthServices =
    retrofit.create(AuthServices::class.java)

  @Provides
  @Singleton
  fun provideAccountServices(retrofit: Retrofit): AccountServices =
    retrofit.create(AccountServices::class.java)

  @Provides
  @Singleton
  fun provideGeneralServices(retrofit: Retrofit): GeneralServices =
    retrofit.create(GeneralServices::class.java)

  @Provides
  @Singleton
  fun provideSearchServices(retrofit: Retrofit): SettingsServices =
    retrofit.create(SettingsServices::class.java)

  @Provides
  @Singleton
  fun provideHomeServices(retrofit: Retrofit): HomeServices =
    retrofit.create(HomeServices::class.java)

  @Provides
  @Singleton
  fun provideIntroServices(retrofit: Retrofit): IntroServices =
    retrofit.create(IntroServices::class.java)

  @Provides
  @Singleton
  fun provideCountriesServices(retrofit: Retrofit): CountriesServices =
    retrofit.create(CountriesServices::class.java)

  @Provides
  @Singleton
  fun provideEducationalServices(retrofit: Retrofit): EducationalServices =
    retrofit.create(EducationalServices::class.java)

  @Provides
  @Singleton
  fun provideStudentServiceServices(retrofit: Retrofit): StudentTeacherServices =
    retrofit.create(StudentTeacherServices::class.java)

  @Provides
  @Singleton
  fun provideTeacherProfileServices(retrofit: Retrofit): TeacherProfileServices =
    retrofit.create(TeacherProfileServices::class.java)

  @Provides
  @Singleton
  fun provideReviewsServices(retrofit: Retrofit): ReviewsServices =
    retrofit.create(ReviewsServices::class.java)

  @Provides
  @Singleton
  fun provideGroupDetailsServices(retrofit: Retrofit): GroupDetailsServices =
    retrofit.create(GroupDetailsServices::class.java)

}