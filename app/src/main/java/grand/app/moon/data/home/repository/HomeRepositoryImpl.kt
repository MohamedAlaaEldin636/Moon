package grand.app.moon.data.home.repository

import grand.app.moon.data.home.data_source.remote.HomeRemoteDataSource
import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.ResponseAppGlobalAnnouncement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
  private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository {
  override suspend fun home(): Resource<BaseResponse<HomeResponse>> = homeRemoteDataSource.home()
  override suspend fun getAppGlobalAnnouncement(): Resource<BaseResponse<ResponseAppGlobalAnnouncement?>> = homeRemoteDataSource.getAppGlobalAnnouncement()
  override suspend fun stories(categoryId : Int?): Resource<BaseResponse<ArrayList<Store>>> = homeRemoteDataSource.stories(categoryId)
  override suspend fun getCategories(): Resource<BaseResponse<ArrayList<CategoryItem>>> = homeRemoteDataSource.getCategories()
  override suspend fun getCategories2(): Resource<BaseResponse<List<ItemCategory>?>> = homeRemoteDataSource.getCategories2()
  override suspend fun getCategoryDetails(id: Int): Resource<BaseResponse<CategoryDetails>>  = homeRemoteDataSource.getCategoryDetails(id)
}