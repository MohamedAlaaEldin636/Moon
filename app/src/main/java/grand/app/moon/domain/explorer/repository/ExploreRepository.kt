package grand.app.moon.domain.explorer.repository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.explorer.entity.ExploreListPaginateData
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface ExploreRepository {
  suspend fun getExplores(): Resource<BaseResponse<ExploreListPaginateData>>
}