package com.structure.base_mvvm.domain.educational.use_case

import com.structure.base_mvvm.domain.account.use_case.UserLocalUseCase
import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.educational.entity.model.Grade
import com.structure.base_mvvm.domain.educational.entity.model.Stage
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep3
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep4
import com.structure.base_mvvm.domain.educational.repository.EducationalRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EducationalUseCase @Inject constructor(
  private val educationalRepository: EducationalRepository,
  private val userLocalUseCase: UserLocalUseCase
) {
  operator fun invoke(): Flow<Resource<BaseResponse<List<Stage>>>> =
    flow {
      emit(Resource.Loading)
      val result = educationalRepository.educationalStages()
      emit(result)
    }.flowOn(Dispatchers.IO)

  operator fun invoke(stage_id: Int): Flow<Resource<BaseResponse<List<Grade>>>> =
    flow {
      emit(Resource.Loading)
      val result = educationalRepository.educationalStageGrades(stage_id)
      emit(result)
    }.flowOn(Dispatchers.IO)

  fun registerStep3(stage_id: Int): Flow<Resource<BaseResponse<*>>> =
    flow {
      emit(Resource.Loading)
      val result =
        educationalRepository.registerStep3(RegisterStep3(educational_stage_id = stage_id))
      if (result is Resource.Success) {
        userLocalUseCase.invoke(Constants.REGISTER_STEP, "4")
      }
      emit(result)
    }.flowOn(Dispatchers.IO)

  fun registerStep4(grade_id: Int): Flow<Resource<BaseResponse<User>>> =
    flow {
      emit(Resource.Loading)
      val result = educationalRepository.registerStep4(RegisterStep4(grade_id = grade_id))
      if (result is Resource.Success) {
        userLocalUseCase.invoke(Constants.REGISTER_STEP, "5")
        userLocalUseCase(result.value.data)
      }
      emit(result)
    }.flowOn(Dispatchers.IO)

}