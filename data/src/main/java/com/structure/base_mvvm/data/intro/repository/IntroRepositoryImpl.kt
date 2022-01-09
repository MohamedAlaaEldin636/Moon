package com.structure.base_mvvm.data.intro.repository

import com.structure.base_mvvm.data.intro.data_source.IntroRemoteDataSource
import com.structure.base_mvvm.domain.intro.entity.AppTutorial
import com.structure.base_mvvm.domain.intro.repository.IntroRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
  private val remoteDataSource: IntroRemoteDataSource
) : IntroRepository {
  override suspend fun intro(): Resource<BaseResponse<List<AppTutorial>>> =
    remoteDataSource.intro()

}