package grand.app.moon.domain.ads.repository
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.domain.home.models.review.Reviews
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface AdsRepository {
  suspend fun getDetails(id: Int,type: Int): Resource<BaseResponse<Advertisement>>
  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest): Resource<BaseResponse<*>>
  suspend fun getProfileAdsList(page: Int , type: Int): Resource<BaseResponse<AdsListPaginateData>>
  suspend fun getAdsList(url : String): Resource<BaseResponse<AdsListPaginateData>>
  suspend fun getAdsSubCategory(url : String): Resource<BaseResponse<SubCategoryResponse>>
  suspend fun getReviews(page:Int,store : String?,advertisement:String?): Resource<BaseResponse<ReviewsPaginateData>>
  suspend fun addReview(url : ReviewRequest): Resource<BaseResponse<*>>
  suspend fun filterDetails(categoryId: Int , subCategoryId: Int?): Resource<BaseResponse<FilterDetails>>
  suspend fun filterResults(url : FilterResultRequest): Resource<BaseResponse<AdsListPaginateData>>

}