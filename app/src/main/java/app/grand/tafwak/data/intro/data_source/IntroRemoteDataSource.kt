package app.grand.tafwak.data.intro.data_source

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class IntroRemoteDataSource @Inject constructor(private val apiService: IntroServices) :
  BaseRemoteDataSource() {

  suspend fun intro() = safeApiCall {
    apiService.intro()
  }
}