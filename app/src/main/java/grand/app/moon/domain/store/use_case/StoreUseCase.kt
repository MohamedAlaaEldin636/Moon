package grand.app.moon.domain.store.use_case

import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.store.repository.StoreRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class StoreUseCase @Inject constructor(
  private val repository: StoreRepository,
) {

  fun follow(followStoreRequest: FollowStoreRequest): Flow<Resource<BaseResponse<*>>> = flow {
    val result = repository.follow(followStoreRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun storeDetails(id: Int): Flow<Resource<BaseResponse<Store>>> = flow {
    emit(Resource.Loading)
    val result = repository.storeDetails(id)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getStores(request: StoreFilterRequest): Flow<Resource<BaseResponse<StoreListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getStores(request)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getFavouriteStores(id: Int): Flow<Resource<BaseResponse<StoreListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getFavouriteStores(id)
    emit(result)
  }.flowOn(Dispatchers.IO)






}
