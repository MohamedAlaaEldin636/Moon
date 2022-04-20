package grand.app.moon.data.explorer.repository

import grand.app.moon.data.explorer.data_source.ExploreRemoteDataSource
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.repository.ExploreRepository
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
  private val remoteDataSource: ExploreRemoteDataSource
) : ExploreRepository {
  override suspend fun getExplores(page: Int): Resource<BaseResponse<ExploreListPaginateData>> = remoteDataSource.explores(page)
  override suspend fun setExploreAction(page: ExploreAction): Resource<BaseResponse<*>> = remoteDataSource.setExploreAction(page)
  override suspend fun setComment(page: ExploreAction): Resource<BaseResponse<Comment>> = remoteDataSource.setComment(page)
  override suspend fun deleteComment(page: Int): Resource<BaseResponse<*>> = remoteDataSource.deleteComment(page)
  override suspend fun getComments(id:Int , page: Int): Resource<BaseResponse<CommentListPaginateData>> = remoteDataSource.getComments(id,page)
  override suspend fun getUsers(id:Int , page: Int): Resource<BaseResponse<UserListPaginateData>> = remoteDataSource.getUsers(id,page)



}