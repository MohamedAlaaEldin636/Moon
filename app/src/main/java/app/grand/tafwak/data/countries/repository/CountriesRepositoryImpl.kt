package app.grand.tafwak.data.countries.repository

import app.grand.tafwak.data.countries.data_source.CountriesRemoteDataSource
import app.grand.tafwak.domain.countries.entity.Country
import app.grand.tafwak.domain.countries.entity.request.RegisterStep2
import app.grand.tafwak.domain.countries.repository.CountriesRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
  private val remoteDataSource: CountriesRemoteDataSource
) : CountriesRepository {
  override suspend fun countries(): Resource<BaseResponse<List<Country>>> = remoteDataSource.countries()
  override suspend fun registerStep2(registerStep2: RegisterStep2): Resource<BaseResponse<*>> =
    remoteDataSource.registerStep2(registerStep2)

}