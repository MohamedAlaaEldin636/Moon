package grand.app.moon.domain.explorer.use_case

import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.explorer.repository.ExploreRepository
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class ExploreUseCase @Inject constructor(
  private val repository: ExploreRepository,
) {

  fun explore(): Flow<Resource<BaseResponse<ExploreListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getExplores()
    emit(result)
  }.flowOn(Dispatchers.IO)


}
