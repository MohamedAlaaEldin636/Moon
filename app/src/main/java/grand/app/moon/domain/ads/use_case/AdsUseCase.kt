package grand.app.moon.domain.ads.use_case

import android.util.Log
import grand.app.moon.domain.ads.ResponseFilterDetails
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.entity.SearchData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.home.models.review.Reviews
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject


class AdsUseCase @Inject constructor(
  private val repo: AdsRepository,
) {

  fun getDetails(id: Int,type: Int): Flow<Resource<BaseResponse<Advertisement>>> = flow {
    emit(Resource.Loading)
    val result = repo.getDetails(id,type)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repo.favourite(addFavouriteAdsRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getProfileAdsList(page: Int, type: Int?): Flow<Resource<BaseResponse<AdsListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repo.getProfileAdsList(page,type)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun setInteraction(request: InteractionRequest): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repo.setInteraction(request)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getAdsList(type:Int? , categoryId: Int?,subCategoryId: Int?,orderBy: Int?,storeId: Int? ,other_options:Int,search:String = "", page: Int?): Flow<Resource<BaseResponse<AdsListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repo.getAdsList(type,categoryId,subCategoryId,orderBy,storeId,other_options,search,page)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getAdsSubCategory(type:Int?, categoryId: Int?, subCategoryId: Int?, orderBy: Int?, storeId: Int?, search:String = "",
                        properties: ArrayList<Property>?, page: Int?): Flow<Resource<BaseResponse<SubCategoryResponse>>> = flow {
    emit(Resource.Loading)
    val result = repo.getAdsSubCategory(type,categoryId,subCategoryId,orderBy,storeId,search,properties,page)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getReviews(page: Int , store: String?, advertisement: String?): Flow<Resource<BaseResponse<ReviewsPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repo.getReviews(page,store,advertisement)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun addReview(request: ReviewRequest): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repo.addReview(request)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun filterDetails(categoryId: Int , subCategoryId: Int?): Flow<Resource<BaseResponse<FilterDetails>>> = flow {
    emit(Resource.Loading)
    val result = repo.filterDetails(categoryId,subCategoryId)
    emit(result)
  }.flowOn(Dispatchers.IO)

  private val TAG = "AdsUseCase"
  fun filterResults(resultRequest : FilterResultRequest,showProgress: Boolean = true): Flow<Resource<BaseResponse<AdsListPaginateData>>> = flow {
    if(showProgress) emit(Resource.Loading)
    Log.d(TAG, "filterResults: ${resultRequest.properties?.size}")
    if(resultRequest.properties?.isEmpty() == true) resultRequest.properties = null
    if(resultRequest.cityIds?.isEmpty() == true) resultRequest.cityIds = null
    Log.d(TAG, "filterResults: ${resultRequest.properties}")
    Log.d(TAG, "filterResults_cityIds: ${resultRequest.cityIds}")
    val result = repo.filterResults(resultRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun search(search: String?,page: Int,showProgress: Boolean): Flow<Resource<BaseResponse<SearchData>>> = flow {
    if(showProgress)emit(Resource.Loading)
    val result = repo.search(search,page)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getFilterDetails2(categoryId: Int, subCategoryId: Int, showProgress: Boolean = true): Flow<Resource<BaseResponse<ResponseFilterDetails?>>> = flow {
    if(showProgress)emit(Resource.Loading)
    val result = repo.getFilterDetails2(categoryId, subCategoryId)
    emit(result)
  }.flowOn(Dispatchers.IO)

	suspend fun getFilterDetails2Suspend(categoryId: Int, subCategoryId: Int) = repo.getFilterDetails2(categoryId, subCategoryId)

	suspend fun addAdvertisement(
		category_id: Int,
		sub_category_id: Int,
		images: List<MultipartBody.Part>,
		title: String,
		latitude: String,
		longitude: String,
		address: String,
		city_id: Int,
		price: Int,
		is_negotiable: Boolean,
		brand_id: Int?,
		description: String,
		propertiesIds: List<Int>,
	) = repo.addAdvertisement(
		category_id, sub_category_id, images, title, latitude, longitude, address, city_id, price,
		if (is_negotiable) 1 else 0, brand_id, description, propertiesIds
	)

	suspend fun getMyAdvertisementDetails(id: Int) = repo.getMyAdvertisementDetails(id)

	suspend fun makeMyAdvertisementPremium(id: Int, packageId: Int) = repo.makeMyAdvertisementPremium(id, packageId)

}
