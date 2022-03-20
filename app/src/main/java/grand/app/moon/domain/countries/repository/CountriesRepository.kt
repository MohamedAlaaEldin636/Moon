package grand.app.moon.domain.countries.repository

import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface CountriesRepository {
  suspend fun countries(): Resource<BaseResponse<List<Country>>>

}