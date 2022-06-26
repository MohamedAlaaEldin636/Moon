package grand.app.moon.domain.home.use_case

import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class HomeUseCase @Inject constructor(
  private val homeRepository: HomeRepository,
) {

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


  fun getCategoryDetails(id: Int): Flow<Resource<BaseResponse<CategoryDetails>>> = flow {
    emit(Resource.Loading)
    val result = homeRepository.getCategoryDetails(id)
    emit(result)
  }.flowOn(Dispatchers.IO)





}
