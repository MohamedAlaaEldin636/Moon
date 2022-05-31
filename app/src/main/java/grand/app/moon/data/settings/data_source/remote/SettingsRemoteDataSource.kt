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

  suspend fun settingsAppInfo(type: String) = safeApiCall {
    apiService.settingsAppInfo(type)
  }



  suspend fun notifications(type: Int) = safeApiCall {
    apiService.notifications(type)
  }
  suspend fun deleteNotification(type: Int) = safeApiCall {
    apiService.deleteNotification(type)
  }

  suspend fun contactApp(contactUsRequest: ContactUsRequest) = safeApiCall {
    apiService.contactApp(contactUsRequest)
  }

}