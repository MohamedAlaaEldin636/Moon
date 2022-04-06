package grand.app.moon.data.map.repository

import grand.app.moon.data.explorer.data_source.ExploreRemoteDataSource
import grand.app.moon.data.map.data_source.MapRemoteDataSource
import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.explorer.repository.ExploreRepository
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.map.repository.MapRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
  private val remoteDataSource: MapRemoteDataSource
) : MapRepository {
  override suspend fun map(type : String): Resource<BaseResponse<List<Store>>> = remoteDataSource.map(type)

}