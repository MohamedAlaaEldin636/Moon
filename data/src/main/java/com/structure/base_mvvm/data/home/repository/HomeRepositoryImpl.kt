package com.structure.base_mvvm.data.home.repository

import com.structure.base_mvvm.data.home.data_source.remote.HomeRemoteDataSource
import com.structure.base_mvvm.domain.home.models.HomeStudentData
import com.structure.base_mvvm.domain.home.repository.HomeRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val remoteDataSource: HomeRemoteDataSource) :
  HomeRepository {
  override suspend fun studentHome(): Resource<BaseResponse<HomeStudentData>> =
    remoteDataSource.homeStudent()
}