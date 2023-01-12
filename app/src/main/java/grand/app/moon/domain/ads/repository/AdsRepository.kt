package grand.app.moon.domain.ads.repository
import grand.app.moon.domain.ads.ResponseFilterDetails
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.ads.entity.SearchData
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.home.models.review.Reviews
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import okhttp3.MultipartBody

interface AdsRepository {
  suspend fun getDetails(id: Int,type: Int): Resource<BaseResponse<Advertisement>>
  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest): Resource<BaseResponse<*>>
  suspend fun getProfileAdsList(page: Int , type: Int?): Resource<BaseResponse<AdsListPaginateData>>
  suspend fun getAdsList(type:Int? , categoryId: Int?,subCategoryId: Int?,orderBy: Int?,storeId: Int?,other_options:Int,search:String = "" , page: Int?): Resource<BaseResponse<AdsListPaginateData>>
  suspend fun getAdsSubCategory(type:Int?, categoryId: Int?, subCategoryId: Int?, orderBy: Int?, storeId: Int?, search:String = ""
                                , properties: ArrayList<Property>?, page: Int?): Resource<BaseResponse<SubCategoryResponse>>
//  suspend fun getAdsSubCategory(url : String): Resource<BaseResponse<SubCategoryResponse>>
  suspend fun getReviews(page:Int,store : String?,advertisement:String?): Resource<BaseResponse<ReviewsPaginateData>>
  suspend fun setInteraction(url : InteractionRequest): Resource<BaseResponse<*>>
  suspend fun addReview(url : ReviewRequest): Resource<BaseResponse<*>>
  suspend fun filterDetails(categoryId: Int , subCategoryId: Int?): Resource<BaseResponse<FilterDetails>>
  suspend fun filterResults(url : FilterResultRequest): Resource<BaseResponse<AdsListPaginateData>>
  suspend fun search(url : String?,page: Int): Resource<BaseResponse<SearchData>>
  suspend fun getFilterDetails2(categoryId: Int, subCategoryId: Int): Resource<BaseResponse<ResponseFilterDetails?>>
  suspend fun addAdvertisement(
	  category_id: Int,
	  sub_category_id: Int,
	  images: List<MultipartBody.Part>,
	  title: String,
	  latitude: String,
	  longitude: String,
	  address: String,
	  city_id: Int,
	  price: Int,
	  is_negotiable: Int,
	  brand_id: Int?,
	  description: String,
	  propertiesIds: List<Pair<Int, String?>>,
  ): Resource<BaseResponse<ResponseMyAdvDetails?>>
	suspend fun getMyAdvertisementDetails(id: Int): Resource<BaseResponse<ResponseMyAdvDetails?>>
	suspend fun deleteAdvertisement(id: Int): Resource<BaseResponse<Any?>>
	suspend fun deleteImageInAdvertisement(id: Int): Resource<BaseResponse<Any?>>

	suspend fun makeMyAdvertisementPremium(id: Int, packageId: Int): Resource<BaseResponse<Any?>>

}