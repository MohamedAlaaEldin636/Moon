package grand.app.moon.core.di.module

import grand.app.moon.data.account.data_source.remote.AccountRemoteDataSource
import grand.app.moon.data.account.repository.AccountRepositoryImpl
import grand.app.moon.data.auth.data_source.remote.AuthRemoteDataSource
import grand.app.moon.data.auth.repository.AuthRepositoryImpl
import grand.app.moon.data.general.data_source.remote.GeneralRemoteDataSource
import grand.app.moon.data.general.repository.GeneralRepositoryImpl
import grand.app.moon.data.home.data_source.remote.HomeRemoteDataSource
import grand.app.moon.data.home.repository.HomeRepositoryImpl
import grand.app.moon.data.intro.data_source.IntroRemoteDataSource
import grand.app.moon.data.intro.repository.IntroRepositoryImpl
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.data.settings.data_source.remote.SettingsRemoteDataSource
import grand.app.moon.data.settings.repository.SettingsRepositoryImpl
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.general.repository.GeneralRepository
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.intro.repository.IntroRepository
import grand.app.moon.domain.settings.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import grand.app.moon.data.ads.data_source.AdsRemoteDataSource
import grand.app.moon.data.ads.repository.AdsRepositoryImpl
import grand.app.moon.data.country.data_source.CountriesRemoteDataSource
import grand.app.moon.data.country.repository.CountriesRepositoryImpl
import grand.app.moon.data.store.data_source.StoreRemoteDataSource
import grand.app.moon.data.store.repository.StoreRepositoryImpl
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.countries.repository.CountriesRepository
import grand.app.moon.domain.store.repository.StoreRepository
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
  fun provideAdsRepository(
    remoteDataSource: AdsRemoteDataSource,
  ): AdsRepository = AdsRepositoryImpl(remoteDataSource)

  @Provides
  @Singleton
  fun provideStoreRepository(
    remoteDataSource: StoreRemoteDataSource,
  ): StoreRepository = StoreRepositoryImpl(remoteDataSource)


  @Provides
  @Singleton
  fun provideCountriesRepository(
    remoteDataSource: CountriesRemoteDataSource,
  ): CountriesRepository = CountriesRepositoryImpl(remoteDataSource)

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

}