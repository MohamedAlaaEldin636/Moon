package app.grand.tafwak.domain.countries.repository

import app.grand.tafwak.domain.countries.entity.Country
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface CountriesRepository {
  suspend fun countries(): Resource<BaseResponse<List<Country>>>

}