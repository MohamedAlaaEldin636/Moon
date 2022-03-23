package grand.app.moon.data.country.repository

import grand.app.moon.data.country.data_source.CountriesRemoteDataSource
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.repository.CountriesRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
  private val countriesRemoteDataSource: CountriesRemoteDataSource
) : CountriesRepository {
  override suspend fun countries(): Resource<BaseResponse<List<Country>>> = countriesRemoteDataSource.countries()

}