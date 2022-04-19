package grand.app.moon.data.store.data_source

import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.*

interface StoreServices {

  @POST("v1/follow")
  suspend fun follow(@Body storeRequest: FollowStoreRequest): BaseResponse<*>

  @GET("v1/stores")
  suspend fun getStores( @QueryMap map: Map<String, String>): BaseResponse<StoreListPaginateData>

  @GET("v1/stores/{id}")
  suspend fun storeDetails(@Path("id") id: Int): BaseResponse<Store>

  @GET("v1/profile/followings")
  suspend fun getFavouriteStores(@Query("page") page: Int): BaseResponse<StoreListPaginateData>


}