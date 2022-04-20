package grand.app.moon.domain.store.repository
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.ReportStoreRequest
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface StoreRepository {
  suspend fun follow(storeRequest: FollowStoreRequest): Resource<BaseResponse<*>>
  suspend fun storeDetails(id: Int): Resource<BaseResponse<Store>>
  suspend fun getFavouriteStores(page: Int): Resource<BaseResponse<StoreListPaginateData>>
  suspend fun report(page: ReportStoreRequest): Resource<BaseResponse<*>>
  suspend fun getStores(request: StoreFilterRequest): Resource<BaseResponse<StoreListPaginateData>>
}