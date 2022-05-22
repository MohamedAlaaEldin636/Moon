package grand.app.moon.data.ads.repository

import grand.app.moon.data.ads.data_source.AdsRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class AdsRepositoryImpl @Inject constructor(
  private val remoteDataSource: AdsRemoteDataSource
) : AdsRepository {

  override
  suspend fun getDetails(id: Int, type: Int) = remoteDataSource.getDetails(id, type)

  override
  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest) =
    remoteDataSource.favourite(addFavouriteAdsRequest)

  override
  suspend fun getProfileAdsList(page: Int, type: Int?) =
    remoteDataSource.getProfileAdsList(page, type)

  override suspend fun getAdsList(
    type: Int?,
    categoryId: Int?,
    subCategoryId: Int?,
    orderBy: Int?,
    storeId: Int?,
    search: String,
    page: Int?
  ) = remoteDataSource.getAdsList(type, categoryId, subCategoryId, orderBy, storeId, search, page)



  override suspend fun getAdsSubCategory(
    type: Int?,
    categoryId: Int?,
    subCategoryId: Int?,
    orderBy: Int?,
    storeId: Int?,
    search: String,
    properties: ArrayList<Property>?,
    page: Int?
  ) = remoteDataSource.getAdsSubCategory(type, categoryId, subCategoryId, orderBy, storeId, search,properties, page)


  override
  suspend fun getReviews(page: Int, store: String?, advertisement: String?) =
    remoteDataSource.getReviews(page, store, advertisement)


  override
  suspend fun addReview(url: ReviewRequest) = remoteDataSource.addReview(url)


  override
  suspend fun setInteraction(url: InteractionRequest) = remoteDataSource.setInteraction(url)

  override
  suspend fun filterDetails(categoryId: Int, subCategoryId: Int?) =
    remoteDataSource.filterDetails(categoryId, subCategoryId)

  override
  suspend fun filterResults(url: FilterResultRequest) = remoteDataSource.filterResults(url)


}