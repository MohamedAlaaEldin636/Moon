package grand.app.moon.data.intro.data_source

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class IntroRemoteDataSource @Inject constructor(private val apiService: IntroServices) :
  BaseRemoteDataSource() {

  suspend fun intro() = safeApiCall {
    apiService.intro()
  }
}