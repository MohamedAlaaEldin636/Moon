package grand.app.moon.data.home.repository

import grand.app.moon.data.home.data_source.remote.HomeRemoteDataSource
import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val remoteDataSource: HomeRemoteDataSource) :
  HomeRepository {
  override suspend fun studentHome(): Resource<BaseResponse<HomeStudentData>> =
    remoteDataSource.homeStudent()
}