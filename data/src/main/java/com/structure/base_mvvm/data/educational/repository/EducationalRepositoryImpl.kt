package com.structure.base_mvvm.data.educational.repository

import com.structure.base_mvvm.data.educational.data_source.EducationalRemoteDataSource
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.educational.entity.model.Grade
import com.structure.base_mvvm.domain.educational.entity.model.Stage
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep3
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep4
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class EducationalRepositoryImpl @Inject constructor(
  private val remoteDataSource: EducationalRemoteDataSource
) : EducationalRepository {
  override suspend fun educationalStages(): Resource<BaseResponse<List<Stage>>> =
    remoteDataSource.educationalStages()

  override suspend fun educationalStageGrades(stage_id: Int): Resource<BaseResponse<List<Grade>>> =
    remoteDataSource.educationalStageGrades(stage_id)

  override suspend fun registerStep3(registerStep3: RegisterStep3): Resource<BaseResponse<*>> =
    remoteDataSource.registerStep3(registerStep3)

  override suspend fun registerStep4(registerStep4: RegisterStep4): Resource<BaseResponse<User>> =
    remoteDataSource.registerStep4(registerStep4)

}