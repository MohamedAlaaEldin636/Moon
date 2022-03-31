package grand.app.moon.domain.settings.repository

import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface SettingsRepository {
  suspend fun onBoard(type: String): Resource<BaseResponse<List<AppTutorial>>>
  suspend fun notifications(): Resource<BaseResponse<NotificationPaginateData>>
  suspend fun settings(type: String): Resource<BaseResponse<List<SettingsData>>>
  suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>>

}