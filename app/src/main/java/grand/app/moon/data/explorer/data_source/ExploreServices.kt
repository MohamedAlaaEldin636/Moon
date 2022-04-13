package grand.app.moon.data.explorer.data_source

import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import retrofit2.http.*

interface ExploreServices {
  @GET("v1/explores")
  suspend fun explores(@Query("page") page : Int): BaseResponse<ExploreListPaginateData>

  @GET("v1/explores/{id}")
  suspend fun getComments(@Path("id") id : Int,@Query("page") page : Int): BaseResponse<CommentListPaginateData>

  @POST("v1/explores")
  suspend fun setExploreAction(@Body page : ExploreAction): BaseResponse<*>


  @GET("v1/users/{id}") //<--need api here osama
  suspend fun getUsers(@Path("id") id : Int,@Query("page") page : Int): BaseResponse<UserListPaginateData>

}