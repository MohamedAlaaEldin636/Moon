package app.grand.tafwak.domain.settings.repository

import app.grand.tafwak.domain.settings.models.ContactUsRequest
import app.grand.tafwak.domain.settings.models.SettingsData
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface SettingsRepository {
  suspend fun settings(type: String, app_type: String): Resource<BaseResponse<SettingsData>>
  suspend fun social(type: String, app_type: String): Resource<BaseResponse<List<SettingsData>>>
  suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>>

}