package grand.app.moon.data.ads.repository

import grand.app.moon.data.ads.data_source.AdsRemoteDataSource
import grand.app.moon.data.auth.data_source.remote.AuthRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
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


}