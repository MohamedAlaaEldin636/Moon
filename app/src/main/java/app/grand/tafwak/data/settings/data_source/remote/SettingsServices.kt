package app.grand.tafwak.data.settings.data_source.remote

import app.grand.tafwak.domain.settings.models.ContactUsRequest
import app.grand.tafwak.domain.settings.models.SettingsData
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SettingsServices {
  @GET("v1/settings")
  suspend fun settings(
    @Query("type") type: String,
    @Query("app_type") app_type: String
  ): BaseResponse<SettingsData>

  @GET("v1/settings")
  suspend fun social(
    @Query("type") type: String,
    @Query("app_type") app_type: String
  ): BaseResponse<List<SettingsData>>

  @POST("v1/contact_app")
  suspend fun contactApp(@Body contactUsRequest: ContactUsRequest): BaseResponse<*>


}