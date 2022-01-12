package com.structure.base_mvvm.domain.countries.repository

import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.countries.entity.request.RegisterStep2
import com.structure.base_mvvm.domain.educational.entity.request.RegisterStep3
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface CountriesRepository {
  suspend fun countries(): Resource<BaseResponse<List<Country>>>
  suspend fun registerStep2(registerStep2: RegisterStep2): Resource<BaseResponse<*>>

}