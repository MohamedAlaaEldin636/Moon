package com.structure.base_mvvm.data.educational.repository

import com.structure.base_mvvm.data.educational.data_source.EducationalRemoteDataSource
import com.structure.base_mvvm.domain.educational.entity.Stage
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class EducationalRepositoryImpl @Inject constructor(
  private val remoteDataSource: EducationalRemoteDataSource
) : EducationalRepository {
  override suspend fun educationalStages(): Resource<BaseResponse<List<Stage>>> = remoteDataSource.educationalStages()
}