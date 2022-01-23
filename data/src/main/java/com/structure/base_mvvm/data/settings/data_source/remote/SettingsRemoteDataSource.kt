package com.structure.base_mvvm.data.settings.data_source.remote

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import com.structure.base_mvvm.domain.settings.models.ContactUsRequest
import javax.inject.Inject

class SettingsRemoteDataSource @Inject constructor(private val apiService: SettingsServices) :
  BaseRemoteDataSource() {
  suspend fun settings(type: String, app_type: String) = safeApiCall {
    apiService.settings(type, app_type)
  }

  suspend fun contactApp(contactUsRequest: ContactUsRequest) = safeApiCall {
    apiService.contactApp(contactUsRequest)
  }

}