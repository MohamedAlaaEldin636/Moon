package grand.app.moon.data.settings.data_source.remote

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.settings.models.ContactUsRequest
import javax.inject.Inject

class SettingsRemoteDataSource @Inject constructor(private val apiService: SettingsServices) :
  BaseRemoteDataSource() {
  suspend fun onBoard(type: String) = safeApiCall {
    apiService.onBoard(type)
  }

  suspend fun settings(type: String) = safeApiCall {
    apiService.settings(type)
  }

  suspend fun notifications() = safeApiCall {
    apiService.notifications()
  }

  suspend fun contactApp(contactUsRequest: ContactUsRequest) = safeApiCall {
    apiService.contactApp(contactUsRequest)
  }

}