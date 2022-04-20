package grand.app.moon.domain.home.repository
import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface HomeRepository {
  suspend fun home(): Resource<BaseResponse<HomeResponse>>
  suspend fun stories(categoryId : Int?): Resource<BaseResponse<ArrayList<Store>>>
  suspend fun getCategories(): Resource<BaseResponse<ArrayList<CategoryItem>>>
  suspend fun getCategoryDetails(id: Int): Resource<BaseResponse<CategoryDetails>>
}