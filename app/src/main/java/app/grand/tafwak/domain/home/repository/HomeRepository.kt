package app.grand.tafwak.domain.home.repository
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface HomeRepository {
  suspend fun studentHome(): Resource<BaseResponse<HomeStudentData>>
}