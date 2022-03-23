package grand.app.moon.domain.home.repository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface HomeRepository {
  suspend fun home(): Resource<BaseResponse<HomeResponse>>
  suspend fun stories(): Resource<BaseResponse<ArrayList<StoryItem>>>
  suspend fun getCategories(): Resource<BaseResponse<ArrayList<CategoryItem>>>
}