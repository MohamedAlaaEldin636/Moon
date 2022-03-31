package grand.app.moon.data.settings.data_source.remote

import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SettingsServices {
  @GET("v1/settings")
  suspend fun onBoard(
    @Query("type") type: String
  ): BaseResponse<List<AppTutorial>>

  @GET("v1/settings")
  suspend fun settings(
    @Query("type") type: String,
  ): BaseResponse<List<SettingsData>>

  @GET("v1/settings")
  suspend fun notifications(): BaseResponse<NotificationPaginateData>


  @POST("v1/contact-us")
  suspend fun contactApp(@Body contactUsRequest: ContactUsRequest): BaseResponse<*>


}