package grand.app.moon.data.map.repository

import grand.app.moon.data.map.data_source.MapRemoteDataSource
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.map.repository.MapRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
  private val remoteDataSource: MapRemoteDataSource
) : MapRepository {
  override suspend fun mapStore(
    type: String, category_id: String?,
    sub_category_id: Int?,
    property_id: Int?,
  ): Resource<BaseResponse<List<Store>>> = remoteDataSource.mapStore(type,category_id,sub_category_id,property_id)

  override suspend fun mapAds(
    type: String,
    property_id: String?,
    subCategoryId: String?,
    categoryId: String?
  ): Resource<BaseResponse<List<Advertisement>>> =
    remoteDataSource.mapAds(type, property_id, subCategoryId, categoryId)

}