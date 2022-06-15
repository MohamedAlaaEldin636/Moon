package grand.app.moon.data.settings.data_source.remote

import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.settings.models.AppInfoResponse
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.*

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
  suspend fun settingsAppInfo(
    @Query("type") type: String,
  ): BaseResponse<AppInfoResponse>

  @GET("v1/notifications")
  suspend fun notifications(@Query("notify_type") type: Int,@Query("page") page: Int): BaseResponse<NotificationPaginateData>

  @DELETE("v1/notifications/{id}")
  suspend fun deleteNotification(@Path("id") id: Int): BaseResponse<*>


  @POST("v1/contact-us")
  suspend fun contactApp(@Body contactUsRequest: ContactUsRequest): BaseResponse<*>


}