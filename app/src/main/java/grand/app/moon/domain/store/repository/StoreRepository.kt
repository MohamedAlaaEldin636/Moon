package grand.app.moon.domain.store.repository
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.*
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface StoreRepository {
  suspend fun follow(storeRequest: FollowStoreRequest): Resource<BaseResponse<*>>
  suspend fun unBlock(storeRequest: FollowStoreRequest): Resource<BaseResponse<*>>
  suspend fun storeDetails(id: Int,type: Int): Resource<BaseResponse<Store>>
  suspend fun getFavouriteStores(page: Int): Resource<BaseResponse<StoreListPaginateData>>
  suspend fun getBlockedStores(page: Int): Resource<BaseResponse<StoreListPaginateData>>
  suspend fun getWhatsappStores(page: Int): Resource<BaseResponse<StoreListPaginateData>>

  suspend fun report(page: ReportStoreRequest): Resource<BaseResponse<*>>
  suspend fun getStores(request: StoreFilterRequest): Resource<BaseResponse<StoreListPaginateData>>
  suspend fun share(request: ShareRequest): Resource<BaseResponse<*>>
  suspend fun getUsersViewFollowing(id:Int , type: String): Resource<BaseResponse<UserListPaginateData>>
  suspend fun storyAction(request: StoryRequest): Resource<BaseResponse<*>>

}