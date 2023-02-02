package grand.app.moon.data.home2

import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.presentation.home.models.ResponseHome
import grand.app.moon.presentation.home.models.ResponseStory
import retrofit2.http.GET

interface Home2Services {

	@GET("v1/check?type=3")
	suspend fun checkAvailabilityForStories(): BaseResponse<Int?>

	@GET("v1/stories")
	suspend fun getNotAllStories(): BaseResponse<List<ResponseStory>?>

	@GET("v1/stories?all=1")
	suspend fun getAllStories(): BaseResponse<List<ResponseStory>?>

	@GET("v1/home")
	suspend fun getHome(): BaseResponse<ResponseHome?>

}
