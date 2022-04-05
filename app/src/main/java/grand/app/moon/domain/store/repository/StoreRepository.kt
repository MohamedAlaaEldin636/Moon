package grand.app.moon.domain.store.repository
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface StoreRepository {
  suspend fun follow(storeRequest: FollowStoreRequest): Resource<BaseResponse<*>>
  suspend fun storeDetails(id: Int): Resource<BaseResponse<Store>>
}