package com.structure.base_mvvm.domain.educational.repository

import com.structure.base_mvvm.domain.auth.entity.model.User
import com.structure.base_mvvm.domain.educational.entity.model.Grade
import com.structure.base_mvvm.domain.educational.entity.model.Stage
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep3
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep4
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface EducationalRepository {
  suspend fun educationalStages(): Resource<BaseResponse<List<Stage>>>
  suspend fun educationalStageGrades(stage_id: Int): Resource<BaseResponse<List<Grade>>>
  suspend fun registerStep3(registerStep3: RegisterStep3): Resource<BaseResponse<*>>
  suspend fun registerStep4(registerStep4: RegisterStep4): Resource<BaseResponse<User>>
}