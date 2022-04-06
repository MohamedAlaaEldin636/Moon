package grand.app.moon.data.explorer.repository

import grand.app.moon.data.explorer.data_source.ExploreRemoteDataSource
import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.explorer.repository.ExploreRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
  private val remoteDataSource: ExploreRemoteDataSource
) : ExploreRepository {
  override suspend fun getExplores(): Resource<BaseResponse<ExploreListPaginateData>> = remoteDataSource.explores()

}