package grand.app.moon.data.home2

import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.map
import grand.app.moon.extensions.mapToNullSuccess
import grand.app.moon.presentation.home.models.ResponseHome
import grand.app.moon.presentation.home.models.ResponseHomeStories
import grand.app.moon.presentation.home.models.ResponseStory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoHome2 @Inject constructor(
	private val remoteDataSource: Home2RemoteDataSource,
	private val useCaseHome: HomeUseCase,
) {

	suspend fun checkAvailabilityForStories() = remoteDataSource.checkAvailabilityForStories()

	suspend fun getNotAllStories(): Resource<BaseResponse<ResponseHomeStories?>> = remoteDataSource.getNotAllStories()

	suspend fun getHome() = remoteDataSource.getHome()

	/*suspend fun getWholeHomeData(): Resource<BaseResponse<WholeHomeData?>> {
		val resourceStories = getNotAllStories()

		return if (resourceStories is Resource.Success && resourceStories.value.data.isNullOrEmpty().not()) {
			val resourceCategories = useCaseHome.getCategoriesSuspend()

			if (resourceCategories is Resource.Success && resourceCategories.value.data.isNullOrEmpty().not()) {
				val responseHome = getHome()

				if (responseHome is Resource.Success && responseHome.value.data != null) {
					responseHome.mapSuccess { baseResponse ->
						baseResponse.map {
							WholeHomeData(
								resourceStories.value.data.orEmpty(),
								resourceCategories.value.data.orEmpty(),
								it!!
							)
						}
					}
				}else {
					responseHome.mapToNullSuccess()
				}
			}else {
				resourceCategories.mapToNullSuccess()
			}
		}else {
			resourceStories.mapToNullSuccess()
		}
	}*/

	data class WholeHomeData(
		var stories: List<ResponseStory>,
		var categories: List<ItemCategory>,
		var responseHome: ResponseHome,
	)

}
