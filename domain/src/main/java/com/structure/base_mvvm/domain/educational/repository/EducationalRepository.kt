package com.structure.base_mvvm.domain.educational.repository

import com.structure.base_mvvm.domain.educational.entity.Stage
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface EducationalRepository {
  suspend fun educationalStages(): Resource<BaseResponse<List<Stage>>>

}