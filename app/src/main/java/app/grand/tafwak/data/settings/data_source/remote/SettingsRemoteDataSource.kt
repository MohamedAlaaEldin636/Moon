package app.grand.tafwak.data.settings.data_source.remote

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import app.grand.tafwak.domain.settings.models.ContactUsRequest
import javax.inject.Inject

class SettingsRemoteDataSource @Inject constructor(private val apiService: SettingsServices) :
  BaseRemoteDataSource() {
  suspend fun settings(type: String, app_type: String) = safeApiCall {
    apiService.settings(type, app_type)
  }

  suspend fun social(type: String, app_type: String) = safeApiCall {
    apiService.social(type, app_type)
  }

  suspend fun contactApp(contactUsRequest: ContactUsRequest) = safeApiCall {
    apiService.contactApp(contactUsRequest)
  }

}