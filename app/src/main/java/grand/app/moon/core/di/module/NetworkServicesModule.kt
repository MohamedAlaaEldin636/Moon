package grand.app.moon.core.di.module

import grand.app.moon.data.account.data_source.remote.AccountServices
import grand.app.moon.data.auth.data_source.remote.AuthServices
import grand.app.moon.data.general.data_source.remote.GeneralServices
import grand.app.moon.data.home.data_source.remote.HomeServices
import grand.app.moon.data.intro.data_source.IntroServices
import grand.app.moon.data.settings.data_source.remote.SettingsServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import grand.app.moon.data.ads.data_source.AdsServices
import grand.app.moon.data.country.data_source.CountriesServices
import grand.app.moon.data.explorer.data_source.ExploreServices
import grand.app.moon.data.home2.Home2Services
import grand.app.moon.data.map.data_source.MapServices
import grand.app.moon.data.packages.PackagesServices
import grand.app.moon.data.shop.ShopServices
import grand.app.moon.data.store.data_source.StoreServices
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
  fun provideMapServices(retrofit: Retrofit): MapServices =
    retrofit.create(MapServices::class.java)

  @Provides
  @Singleton
  fun provideCountriesServices(retrofit: Retrofit): CountriesServices =
    retrofit.create(CountriesServices::class.java)

  @Provides
  @Singleton
  fun provideExploreServices(retrofit: Retrofit): ExploreServices =
    retrofit.create(ExploreServices::class.java)

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
  fun provideAdsServices(retrofit: Retrofit): AdsServices =
    retrofit.create(AdsServices::class.java)

  @Provides
  @Singleton
  fun providePackagesServices(retrofit: Retrofit): PackagesServices =
    retrofit.create(PackagesServices::class.java)

  @Provides
  @Singleton
  fun provideShopServices(retrofit: Retrofit): ShopServices =
    retrofit.create(ShopServices::class.java)

  @Provides
  @Singleton
  fun provideHome2Services(retrofit: Retrofit): Home2Services =
    retrofit.create(Home2Services::class.java)

  @Provides
  @Singleton
  fun provideStoreServices(retrofit: Retrofit): StoreServices =
    retrofit.create(StoreServices::class.java)

  @Provides
  @Singleton
  fun provideIntroServices(retrofit: Retrofit): IntroServices =
    retrofit.create(IntroServices::class.java)


}