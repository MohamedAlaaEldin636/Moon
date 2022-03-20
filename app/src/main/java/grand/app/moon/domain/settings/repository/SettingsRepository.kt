package grand.app.moon.domain.settings.repository

import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface SettingsRepository {
  suspend fun settings(type: String, app_type: String): Resource<BaseResponse<SettingsData>>
  suspend fun social(type: String, app_type: String): Resource<BaseResponse<List<SettingsData>>>
  suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>>

}