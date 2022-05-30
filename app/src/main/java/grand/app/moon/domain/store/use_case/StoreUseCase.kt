package grand.app.moon.domain.store.use_case

import android.util.Log
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.*
import grand.app.moon.domain.store.repository.StoreRepository
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class StoreUseCase @Inject constructor(
  private val repository: StoreRepository,
) {

  private  val TAG = "StoreUseCase"
  fun follow(followStoreRequest: FollowStoreRequest): Flow<Resource<BaseResponse<*>>> = flow {
    Log.d(TAG, "follow: STORING")
    val result = repository.follow(followStoreRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun unBlock(followStoreRequest: FollowStoreRequest): Flow<Resource<BaseResponse<*>>> = flow {
    Log.d(TAG, "follow: STORING")
    val result = repository.unBlock(followStoreRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun unBlock(followStoreRequest: ArrayList<Int>): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repository.unBlock(followStoreRequest)
    emit(result)
  }.flowOn(Dispatchers.IO)



  fun storeDetails(id: Int, type: Int): Flow<Resource<BaseResponse<Store>>> = flow {
    emit(Resource.Loading)
    val result = repository.storeDetails(id,type)
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

  fun getBlockedStores(id: Int): Flow<Resource<BaseResponse<StoreListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getBlockedStores(id)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getWhatsappStores(id: Int): Flow<Resource<BaseResponse<StoreListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getWhatsappStores(id)
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun report(id: ReportStoreRequest): Flow<Resource<BaseResponse<*>>> = flow {
    emit(Resource.Loading)
    val result = repository.report(id)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun share(id: ShareRequest): Flow<Resource<BaseResponse<*>>> = flow {
    val result = repository.share(id)
    emit(result)
  }.flowOn(Dispatchers.IO)

  fun getUsersViewFollowing(id: Int, type: String): Flow<Resource<BaseResponse<UserListPaginateData>>> = flow {
    emit(Resource.Loading)
    val result = repository.getUsersViewFollowing(id,type)
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun storyAction(id: StoryRequest): Flow<Resource<BaseResponse<*>>> = flow {
    val result = repository.storyAction(id)
    emit(result)
  }.flowOn(Dispatchers.IO)




}
