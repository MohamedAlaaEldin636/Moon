package grand.app.moon.data.ads.repository

import grand.app.moon.data.ads.data_source.AdsRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.review.ReviewRequest
import javax.inject.Inject

class AdsRepositoryImpl @Inject constructor(
  private val remoteDataSource: AdsRemoteDataSource
) : AdsRepository {

  override
  suspend fun getDetails(id: Int , type : Int) = remoteDataSource.getDetails(id,type)

  override
  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest) = remoteDataSource.favourite(addFavouriteAdsRequest)

  override
  suspend fun getProfileAdsList(page:Int , type: Int) = remoteDataSource.getProfileAdsList(page,type)

  override
  suspend fun getAdsList(url : String) = remoteDataSource.getAdsList(url)


  override
  suspend fun getAdsSubCategory(url : String) = remoteDataSource.getAdsSubCategory(url)


  override
  suspend fun getReviews(page:Int,advertisement:Int) = remoteDataSource.getReviews(page,advertisement)



  override
  suspend fun addReview(url:ReviewRequest) = remoteDataSource.addReview(url)




}