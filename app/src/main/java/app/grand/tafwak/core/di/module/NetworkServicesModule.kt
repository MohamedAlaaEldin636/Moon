package app.grand.tafwak.core.di.module

import com.structure.base_mvvm.data.account.data_source.remote.AccountServices
import com.structure.base_mvvm.data.auth.data_source.remote.AuthServices
import com.structure.base_mvvm.data.countries.data_source.CountriesServices
import com.structure.base_mvvm.data.educational.data_source.EducationalServices
import com.structure.base_mvvm.data.general.data_source.remote.GeneralServices
import com.structure.base_mvvm.data.home.data_source.remote.HomeServices
import com.structure.base_mvvm.data.intro.data_source.IntroServices
import com.structure.base_mvvm.data.settings.data_source.remote.SettingsServices
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

}