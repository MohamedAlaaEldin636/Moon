package grand.app.moon.data.ads.data_source

import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.VerifyAccountRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.utils.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface AdsServices {

  @GET("v1/advertisements/{id}")
  suspend fun details(@Path("id") id : Int , @Query("type") type : Int): BaseResponse<Advertisement>

  @POST("v1/favorite")
  suspend fun favourite(@Body addFavouriteAdsRequest: AddFavouriteAdsRequest): BaseResponse<*>

  @GET("v1/profile/advertisements")
  suspend fun getAdsList(@Query(" type") type : Int): BaseResponse<AdsListPaginateData>
}