package grand.app.moon.domain.ads.repository
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface AdsRepository {
  suspend fun getDetails(id: Int,type: Int): Resource<BaseResponse<Advertisement>>
  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest): Resource<BaseResponse<*>>
  suspend fun getAdsList(type: Int): Resource<BaseResponse<AdsListPaginateData>>

}