package grand.app.moon.core.di.module

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import grand.app.moon.BuildConfig
import grand.app.moon.data.local.preferences.AppPreferences
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import grand.app.moon.core.MyApplication
import grand.app.moon.presentation.base.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

  const val REQUEST_TIME_OUT: Long = 60

  private const val TAG = "RetrofitModule"

//  @Provides
//  fun provideAppPreference(): SharedPreferences {
//    return MyApplication.instance.getSharedPreferences(
//      AppPreferences.APP_PREFERENCES_NAME,
//      Context.MODE_PRIVATE
//    )
//  }

  @Provides
  @Singleton
  fun provideHeadersInterceptor(appPreferences: AppPreferences) = run {

//    var userToken = appPreferences.getLocal(Constants.TOKEN)
//    var token2 = appPreferences.getUser().token
//    Log.d(TAG, "provideHeadersInterceptor: $userToken")
//    Log.d(TAG, "provideHeadersInterceptor___: $token2")
//    GlobalScope.launch {
//      withContext(Dispatchers.IO) {
//        awaitAll(
//          async {
//            appPreferences.getCountryId().collect { country_id ->
//              countryId = country_id
//            }
//          }, async {
//            appPreferences.getUserToken().collect { token ->
//              userToken = token
//            }
//          }
//        )
//      }
//    }


    Log.d(TAG, "provideHeadersInterceptor-language: ${AppPreferences.LANGUAGE}")
    Log.d(TAG, "intercept-userToken-here: ${appPreferences.getUserToken()}")
//    var lang = AppPreferences.LANGUAGE
    if(appPreferences.getLocal(Constants.LANGUAGE).isEmpty())
      appPreferences.setLocal(Constants.LANGUAGE,Constants.DEFAULT_LANGUAGE)

//    Log.d(TAG, "provideHeadersInterceptor: $lang")
//    Log.d(TAG, "provideHeadersInterceptor: ${appPreferences.getLocal(Constants.LANGUAGE)}")
    Interceptor { chain ->
//      Log.e("provideHeadersInterceptor", "provideHeadersInterceptor: $userToken :,token:$token2:  $countryId")
      val request = chain.request().newBuilder()
      if (appPreferences.getIsLoggedIn())
        request.addHeader("Authorization", "Bearer ${appPreferences.getLocal(Constants.TOKEN)}")
      request.addHeader("language", appPreferences.getLocal(Constants.LANGUAGE))
      request.addHeader("platform", "1")
      request.addHeader("Accept", "application/json")
      request.addHeader("countryId", appPreferences.getLocal(Constants.COUNTRY_ID))

      chain.proceed(request.build())
    }
  }

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

//  @Provides
//  fun provideRequestInterceptor(prefs: AppPreferences) : RequestInterceptor {
//    return RequestInterceptor(prefs)
//  }
//
//
//
//  class RequestInterceptor constructor(private val pref: AppPreferences) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//      var userToken = pref.getLocal(Constants.TOKEN)
//      val token = pref.getUser().token
//      var countryId = pref.getLocal(Constants.COUNTRY_ID)
//      if(countryId.isEmpty()) countryId = "1"
//
//      Log.d(TAG, "intercept-userToken: $userToken")
//      Log.d(TAG, "intercept-token: $token")
//      Log.d(TAG, "intercept-token-language: ${pref.getLocal(Constants.LANGUAGE)}")
//      val newRequest = chain.request().newBuilder()
//        .addHeader("Authorization", "Bearer $userToken")
//        .addHeader("countryId", countryId)
//        .addHeader("platform", "1")
//        .addHeader("language", pref.getLocal(Constants.LANGUAGE))
//
//      .build()
//      return chain.proceed(newRequest)
//    }
//  }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    headersInterceptor: Interceptor,
    logging: HttpLoggingInterceptor,
//    requestInterceptor: RequestInterceptor,
    @ApplicationContext context: Context
  ): OkHttpClient {
    return if (BuildConfig.DEBUG) {
      OkHttpClient.Builder()
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
        .addNetworkInterceptor(logging)
        .addInterceptor(ChuckInterceptor(context))
//        .addInterceptor(requestInterceptor)
        .build()
    } else {
      OkHttpClient.Builder()
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
//        .addInterceptor(requestInterceptor)
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