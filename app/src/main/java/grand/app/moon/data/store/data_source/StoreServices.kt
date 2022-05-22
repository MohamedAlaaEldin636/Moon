package grand.app.moon.data.store.data_source

import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.*
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.*

interface StoreServices {

  @POST("v1/follow")
  suspend fun follow(@Body storeRequest: FollowStoreRequest): BaseResponse<*>

  @GET("v1/stores")
  suspend fun getStores( @QueryMap map: Map<String, String>): BaseResponse<StoreListPaginateData>

  @GET("v1/stores/{id}")
  suspend fun storeDetails(@Path("id") id: Int,@Query("type")type: Int): BaseResponse<Store>

  @GET("v1/profile/followings")
  suspend fun getFavouriteStores(@Query("page") page: Int): BaseResponse<StoreListPaginateData>

  @POST("v1/store/unblock")
  suspend fun unBlock(@Body storeRequest: FollowStoreRequest): BaseResponse<*>

  @GET("v1/profile/blocked")
  suspend fun getBlockedStores(@Query("page") page: Int): BaseResponse<StoreListPaginateData>


  @GET("v1/profile/whatsapp")
  suspend fun getWhatsappStores(@Query("page") page: Int): BaseResponse<StoreListPaginateData>


  @POST("v1/store/report_block")
  suspend fun report(@Body page: ReportStoreRequest): BaseResponse<*>

  @POST("v1/stories")
  suspend fun storyAction(@Body storeRequest: StoryRequest): BaseResponse<*>

  @POST("v1/share")
  suspend fun share(@Body page: ShareRequest): BaseResponse<*>

  @GET("v1/stores/{id}/{type}")
  suspend fun getUsersViewFollowing( @Path("id") storeId: Int ,@Path("type") type: String ): BaseResponse<UserListPaginateData>

}