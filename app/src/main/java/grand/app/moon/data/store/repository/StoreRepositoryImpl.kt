package grand.app.moon.data.store.repository

import grand.app.moon.data.store.data_source.StoreRemoteDataSource
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.domain.store.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
  private val remoteDataSource: StoreRemoteDataSource
) : StoreRepository {

  override
  suspend fun follow(storeRequest: FollowStoreRequest) = remoteDataSource.follow(storeRequest)


  override
  suspend fun storeDetails(id: Int) = remoteDataSource.storeDetails(id)


  override
  suspend fun getStores(request: StoreFilterRequest) = remoteDataSource.getStores(request)



  override
  suspend fun getFavouriteStores(page: Int) = remoteDataSource.getFavouriteStores(page)


}