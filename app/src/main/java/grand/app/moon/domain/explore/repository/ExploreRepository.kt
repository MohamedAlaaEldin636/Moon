package grand.app.moon.domain.explore.repository
import androidx.paging.PagingData
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.paging.MABasePaging
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {
  suspend fun getExplores(page:Int): Resource<BaseResponse<ExploreListPaginateData>>
  suspend fun setExploreAction(page:ExploreAction): Resource<BaseResponse<*>>
  suspend fun setComment(page:ExploreAction): Resource<BaseResponse<Comment>>
  suspend fun deleteComment(page:Int): Resource<BaseResponse<*>>
  fun getComments(id: Int ): Flow<PagingData<Comment>>
  suspend fun getUsers(id: Int , page: Int): Resource<BaseResponse<UserListPaginateData>>
}