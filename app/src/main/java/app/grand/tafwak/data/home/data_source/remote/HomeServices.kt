package app.grand.tafwak.data.home.data_source.remote

import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.GET

interface HomeServices {
  @GET("v1/student/home")
  suspend fun homeStudent(): BaseResponse<HomeStudentData>

}