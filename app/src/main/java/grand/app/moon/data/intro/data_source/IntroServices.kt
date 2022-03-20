package grand.app.moon.data.intro.data_source

import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET

interface IntroServices {

  @GET("v1/settings?type=welcome&app_type=general")
  suspend fun intro(): BaseResponse<List<AppTutorial>>

}