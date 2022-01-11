package com.structure.base_mvvm.domain.countries.repository

import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface CountriesRepository {
  suspend fun countries(): Resource<BaseResponse<List<Country>>>

}