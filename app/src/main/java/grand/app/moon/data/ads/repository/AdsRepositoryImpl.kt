package grand.app.moon.data.ads.repository

import grand.app.moon.data.ads.data_source.AdsRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.entity.SearchData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import okhttp3.MultipartBody
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
    other_options:Int,
    search: String,
    page: Int?
  ) = remoteDataSource.getAdsList(type, categoryId, subCategoryId, orderBy, storeId,other_options, search, page)



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


  override
  suspend fun search(url: String?,page:Int) = remoteDataSource.search(url,page)

  override suspend fun getFilterDetails2(categoryId: Int, subCategoryId: Int) = remoteDataSource.getFilterDetails2(categoryId, subCategoryId)

	override suspend fun addAdvertisement(
		category_id: Int,
		sub_category_id: Int,
		images: List<MultipartBody.Part>,
		latitude: String,
		longitude: String,
		address: String,
		city_id: Int,
		price: Int,
		is_negotiable: Int,
		brand_id: Int?,
		description: String,
		propertiesIds: List<Int>,
	) = remoteDataSource.addAdvertisement(category_id, sub_category_id, images, latitude, longitude, address, city_id, price, is_negotiable, brand_id, description, propertiesIds)


}