package grand.app.moon.domain.explore.repository
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface ExploreRepository {
  suspend fun getExplores(page:Int): Resource<BaseResponse<ExploreListPaginateData>>
  suspend fun setExploreAction(page:ExploreAction): Resource<BaseResponse<*>>
  suspend fun getComments(id: Int , page: Int): Resource<BaseResponse<CommentListPaginateData>>
  suspend fun getUsers(id: Int , page: Int): Resource<BaseResponse<UserListPaginateData>>
}