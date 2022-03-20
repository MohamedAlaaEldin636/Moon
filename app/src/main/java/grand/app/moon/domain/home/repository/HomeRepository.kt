package grand.app.moon.domain.home.repository
import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface HomeRepository {
  suspend fun studentHome(): Resource<BaseResponse<HomeStudentData>>
}