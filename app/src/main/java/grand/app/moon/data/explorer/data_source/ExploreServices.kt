package grand.app.moon.data.explorer.data_source

import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.comment.entity.CommentListPaginateData
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.user.entity.UserListPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.helpers.paging.MABasePaging
import retrofit2.http.*

interface ExploreServices {
  @GET("v1/explores")
  suspend fun explores(@Query("page") page : Int): BaseResponse<ExploreListPaginateData>

  @GET("v1/explores/{id}/comments")
  suspend fun getComments(@Path("id") id : Int,@Query("page") page : Int): MABaseResponse<MABasePaging<Comment>>

  @POST("v1/explores")
  suspend fun setExploreAction(@Body page : ExploreAction): BaseResponse<*>


  @POST("v1/explores/comments")
  suspend fun setComment(@Body page : ExploreAction): BaseResponse<Comment>


  @DELETE("v1/explores/comments/{id}")
  suspend fun deleteComment(@Path("id") id: Int): BaseResponse<*>



  @GET("v1/explores/{id}/comments")
  suspend fun CommentListPaginateData(@Path("id") id : Int,@Query("page") page : Int): BaseResponse<CommentListPaginateData>



  @GET("v1/explores/{id}/likes") //<--need api here osama
  suspend fun getUsers(@Path("id") id : Int,@Query("page") page : Int): BaseResponse<UserListPaginateData>

}