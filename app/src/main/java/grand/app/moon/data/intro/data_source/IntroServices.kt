package grand.app.moon.data.intro.data_source

import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IntroServices {

  @GET("v1/settings")
  suspend fun intro(@Query("type") type: Int): BaseResponse<List<AppTutorial>>

}