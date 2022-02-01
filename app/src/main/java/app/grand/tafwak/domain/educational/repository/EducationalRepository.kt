package app.grand.tafwak.domain.educational.repository

import app.grand.tafwak.domain.auth.entity.model.User
import app.grand.tafwak.domain.educational.entity.model.Grade
import app.grand.tafwak.domain.educational.entity.model.Stage
import app.grand.tafwak.domain.educational.entity.request.RegisterStep3
import app.grand.tafwak.domain.educational.entity.request.RegisterStep4
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface EducationalRepository {
  suspend fun educationalStages(): Resource<BaseResponse<List<Stage>>>
  suspend fun educationalStageGrades(stage_id: Int): Resource<BaseResponse<List<Grade>>>
  suspend fun registerStep3(registerStep3: RegisterStep3): Resource<BaseResponse<*>>
  suspend fun registerStep4(registerStep4: RegisterStep4): Resource<BaseResponse<User>>
}