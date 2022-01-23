package com.structure.base_mvvm.domain.home.repository
import com.structure.base_mvvm.domain.home.models.HomeStudentData
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface HomeRepository {
  suspend fun studentHome(): Resource<BaseResponse<HomeStudentData>>
}