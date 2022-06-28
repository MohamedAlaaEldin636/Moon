package grand.app.moon.domain.explore.use_case

import androidx.paging.PagingData
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.repository.ExploreRepository
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.paging.MABasePaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class ExploreUseCase @Inject constructor(
  private val repository: ExploreRepository,
) {

  fun explore(page: Int): Flow<Resource<BaseResponse<ExploreListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getExplores(page)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun setExploreAction(exploreAction: ExploreAction,withLoader : Boolean = false): Flow<Resource<BaseResponse<*>>> = flow {
    if(withLoader) emit(Resource.Loading)
    val result = repository.setExploreAction(exploreAction)
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun getComments(id: Int): Flow<PagingData<Comment>> = repository.getComments(id)
  fun commentListPaginateData(id: Int,page: Int): Flow<Resource<BaseResponse<CommentListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.CommentListPaginateData(id,page)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun setComment(exploreAction: ExploreAction,withLoader : Boolean = false): Flow<Resource<BaseResponse<Comment>>> = flow {
    if(withLoader) emit(Resource.Loading)
    val result = repository.setComment(exploreAction)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun deleteComment(exploreAction: Int): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repository.deleteComment(exploreAction)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getUsers(id: Int,page: Int): Flow<Resource<BaseResponse<UserListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getUsers(id,page)
    emit(result)
  }.flowOn(Dispatchers.IO)



}
