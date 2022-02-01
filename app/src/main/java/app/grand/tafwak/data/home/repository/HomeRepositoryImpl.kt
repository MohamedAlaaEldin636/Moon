package app.grand.tafwak.data.home.repository

import app.grand.tafwak.data.home.data_source.remote.HomeRemoteDataSource
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.home.repository.HomeRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val remoteDataSource: HomeRemoteDataSource) :
  HomeRepository {
  override suspend fun studentHome(): Resource<BaseResponse<HomeStudentData>> =
    remoteDataSource.homeStudent()
}