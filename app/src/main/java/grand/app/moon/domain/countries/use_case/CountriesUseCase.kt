package grand.app.moon.domain.countries.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.repository.CountriesRepository
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.intro.repository.IntroRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CountriesUseCase @Inject constructor(
  private val countryRepository: CountriesRepository) {

  operator fun invoke(): Flow<Resource<BaseResponse<List<Country>>>> = flow {
    emit(Resource.Loading)
    val result = countryRepository.countries()
    emit(result)
  }.flowOn(Dispatchers.IO)
}