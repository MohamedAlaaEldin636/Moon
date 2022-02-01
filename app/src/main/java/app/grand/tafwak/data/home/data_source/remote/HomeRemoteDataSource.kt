package app.grand.tafwak.data.home.data_source.remote

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(private val apiService: HomeServices) :
  BaseRemoteDataSource() {

  suspend fun homeStudent() = safeApiCall {
    apiService.homeStudent()
  }
}