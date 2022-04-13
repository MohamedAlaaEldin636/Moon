package grand.app.moon.domain.ads.use_case

import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.story.entity.StoryItem
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
}
