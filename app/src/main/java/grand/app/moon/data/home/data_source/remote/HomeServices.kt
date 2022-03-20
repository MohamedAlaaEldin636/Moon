package grand.app.moon.data.home.data_source.remote

import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.GET

interface HomeServices {
  @GET("v1/student/home")
  suspend fun homeStudent(): BaseResponse<HomeStudentData>

}