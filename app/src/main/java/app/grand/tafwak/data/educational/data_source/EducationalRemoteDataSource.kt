package app.grand.tafwak.data.educational.data_source

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import app.grand.tafwak.domain.educational.entity.request.RegisterStep3
import app.grand.tafwak.domain.educational.entity.request.RegisterStep4
import javax.inject.Inject

class EducationalRemoteDataSource @Inject constructor(private val apiService: EducationalServices) :
  BaseRemoteDataSource() {

  suspend fun educationalStages() = safeApiCall {
    apiService.educationalStages()
  }

  suspend fun educationalStageGrades(stage_id: Int) = safeApiCall {
    apiService.educationalStageGrade(stage_id)
  }

  suspend fun registerStep3(registerStep3: RegisterStep3) = safeApiCall {
    apiService.registerStep3(registerStep3)
  }

  suspend fun registerStep4(registerStep4: RegisterStep4) = safeApiCall {
    apiService.registerStep4(registerStep4)
  }

}