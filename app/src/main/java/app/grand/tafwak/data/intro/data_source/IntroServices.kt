package app.grand.tafwak.data.intro.data_source

import app.grand.tafwak.domain.intro.entity.AppTutorial
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.GET

interface IntroServices {

  @GET("v1/settings?type=welcome&app_type=general")
  suspend fun intro(): BaseResponse<List<AppTutorial>>

}