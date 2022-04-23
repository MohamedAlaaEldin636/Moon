package grand.app.moon.domain.ads.use_case

import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

  fun getProfileAdsList(page: Int, type: Int): Flow<Resource<BaseResponse<AdsListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repo.getProfileAdsList(page,type)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getAdsList(url : String): Flow<Resource<BaseResponse<AdsListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repo.getAdsList(url)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getAdsSubCategory(url : String): Flow<Resource<BaseResponse<SubCategoryResponse>>> = flow {
    emit(Resource.Loading)
    val result = repo.getAdsSubCategory(url)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getReviews(page: Int , advertisement: Int): Flow<Resource<BaseResponse<ReviewsPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repo.getReviews(page,advertisement)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun addReview(request: ReviewRequest): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repo.addReview(request)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun filterDetails(categoryId: Int , subCategoryId: Int): Flow<Resource<BaseResponse<FilterDetails>>> = flow {
    emit(Resource.Loading)
    val result = repo.filterDetails(categoryId,subCategoryId)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun filterResults(resultRequest : FilterResultRequest): Flow<Resource<BaseResponse<AdsListPaginateData>>> = flow {
    emit(Resource.Loading)
    if(resultRequest.properties?.isEmpty() == true) resultRequest.properties = null
    if(resultRequest.cityIds?.isEmpty() == true) resultRequest.cityIds = null
    val result = repo.filterResults(resultRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)
}
