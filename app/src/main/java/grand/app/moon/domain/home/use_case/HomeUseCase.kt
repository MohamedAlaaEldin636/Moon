package grand.app.moon.domain.home.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.ResponseAnnouncement
import grand.app.moon.domain.home.models.ResponseAppGlobalAnnouncement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeUseCase @Inject constructor(
  private val homeRepository: HomeRepository,
  private val accountRepository: AccountRepository,
) {

	fun getAppGlobalAnnouncement(showProgress: Boolean = true): Flow<Resource<BaseResponse<ResponseAppGlobalAnnouncement?>>> = flow {
		if(showProgress) emit(Resource.Loading)
		val result = homeRepository.getAppGlobalAnnouncement()
		emit(result)
	}.flowOn(Dispatchers.IO)

	/**
	 * @return `null` show nothing else show the announcement
	 */
	fun getAnnouncement(showProgress: Boolean = true): Flow<Resource<BaseResponse<ResponseAnnouncement?>>> = flow {
		if(showProgress) emit(Resource.Loading)
		val result = homeRepository.getAnnouncement()

		val newResult = result.mapSuccess {
			val toBeShownAnnouncement = accountRepository.checkAnnouncementAndClearIfNullSaveIfNewOrIncrementCountIfExistsAndGetOnlyIfShouldShow(
				it.data
			)

			// Only maps success
			it.copy(data = toBeShownAnnouncement)
		}

		emit(newResult)
	}.flowOn(Dispatchers.IO)

  fun home(showProgress: Boolean = true): Flow<Resource<BaseResponse<HomeResponse>>> = flow {
    if(showProgress) emit(Resource.Loading)
    val result = homeRepository.home()
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun getStories(categoryId : Int?): Flow<Resource<BaseResponse<ArrayList<Store>>>> = flow {
    val result = homeRepository.stories(categoryId)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getCategories(): Flow<Resource<BaseResponse<ArrayList<CategoryItem>>>> = flow {
    val result = homeRepository.getCategories()
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getCategories2(): Flow<Resource<BaseResponse<List<ItemCategory>?>>> = flow {
	  emit(Resource.Loading)
    val result = homeRepository.getCategories2()
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun getCategoryDetails(id: Int): Flow<Resource<BaseResponse<CategoryDetails>>> = flow {
    emit(Resource.Loading)
    val result = homeRepository.getCategoryDetails(id)
    emit(result)
  }.flowOn(Dispatchers.IO)

	suspend fun checkAvailableAdvertisements() = homeRepository.getCheckAvailability(Availability.ADVERTISEMENT.apiValue)

	suspend fun getCheckAvailabilityForPremiumAds() = homeRepository.getCheckAvailabilityForPremiumAds()

}

private enum class Availability(val apiValue: Int) {
	ADVERTISEMENT(1),
	EXPLORE(2),
	STORIES(3),
}
