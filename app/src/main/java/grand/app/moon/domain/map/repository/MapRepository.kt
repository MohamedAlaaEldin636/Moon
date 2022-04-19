package grand.app.moon.domain.map.repository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface MapRepository {
  suspend fun mapStore(type:String): Resource<BaseResponse<List<Store>>>
  suspend fun mapAds(type:String,property_id: String?,subCategoryId: String?,categoryId: String?): Resource<BaseResponse<List<Advertisement>>>
}