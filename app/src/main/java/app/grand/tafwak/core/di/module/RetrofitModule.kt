package app.grand.tafwak.core.di.module

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.structure.base_mvvm.BuildConfig
import com.structure.base_mvvm.data.local.preferences.AppPreferences
import com.readystatesoftware.chuck.ChuckInterceptor
import com.structure.base_mvvm.domain.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

  const val REQUEST_TIME_OUT: Long = 60

  @Provides
  @Singleton
  fun provideHeadersInterceptor(appPreferences: AppPreferences) =
    Interceptor { chain ->
      Log.e("JWT", "provideHeadersInterceptor: ${appPreferences.getLocal(Constants.TOKEN)}")
      chain.proceed(
        chain.request().newBuilder()
          .addHeader("Authorization", "Bearer ${appPreferences.getLocal(Constants.TOKEN)}")
          .addHeader("countryId", appPreferences.getLocal(Constants.COUNTRY_ID))
          .addHeader("Accept", "application/json")
          .addHeader("language", "ar")
          .build()
      )
    }

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    headersInterceptor: Interceptor,
    logging: HttpLoggingInterceptor,
    @ApplicationContext context: Context
  ): OkHttpClient {
    return if (BuildConfig.DEBUG) {
      OkHttpClient.Builder()
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
        .addNetworkInterceptor(logging)
        .addInterceptor(ChuckInterceptor(context))
        .build()
    } else {
      OkHttpClient.Builder()
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
        .build()
    }
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder()
      .setLenient()
      .serializeNulls() // To allow sending null values
      .create()
  }

  @Provides
  @Singleton
  fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BuildConfig.API_BASE_URL)
    .build()
}