package grand.app.moon.domain.home.use_case

import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class HomeUseCase @Inject constructor(
  private val homeRepository: HomeRepository,
) {

  operator fun invoke(): Flow<Resource<BaseResponse<HomeResponse>>> = flow {
    emit(Resource.Loading)
    val result = homeRepository.home()
    emit(result)
  }.flowOn(Dispatchers.IO)



  fun getStories(): Flow<Resource<BaseResponse<ArrayList<StoryItem>>>> = flow {
    val result = homeRepository.stories()
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getCategories(): Flow<Resource<BaseResponse<ArrayList<CategoryItem>>>> = flow {
    val result = homeRepository.getCategories()
    emit(result)
  }.flowOn(Dispatchers.IO)


}
