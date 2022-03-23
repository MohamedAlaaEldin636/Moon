package grand.app.moon.data.country.data_source

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class CountriesRemoteDataSource @Inject constructor(private val apiService: CountriesServices) :
  BaseRemoteDataSource() {

  suspend fun countries() = safeApiCall {
    apiService.countries()
  }
}