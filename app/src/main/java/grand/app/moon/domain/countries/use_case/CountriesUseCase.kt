package grand.app.moon.domain.countries.use_case

import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.repository.CountriesRepository
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.intro.repository.IntroRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CountriesUseCase @Inject constructor(
  private val countryRepository: CountriesRepository,
  private val accountRepository: AccountRepository,
) {

  operator fun invoke(): Flow<Resource<BaseResponse<List<Country>>>> = flow {
    emit(Resource.Loading)
    val result = countryRepository.countries()
    emit(result)
  }.flowOn(Dispatchers.IO)

	suspend fun getSuspend() = countryRepository.countries().mapSuccess {
		BaseResponse<List<Country>?>(it.data, it.message, it.code)
	}

	suspend fun getCities() = countryRepository.countries().mapSuccess { response ->
		val countryId = kotlin.runCatching {
			accountRepository.getKeyFromLocal(Constants.COUNTRY_ID)
		}.getOrNull()

		BaseResponse<List<Country>?>(
			response.data?.firstOrNull { it.id?.toString() == countryId }?.cities,
			response.message,
			response.code
		)
	}

	suspend fun getCitiesAndAreas() = countryRepository.countries().mapSuccess { response ->
		val countryId = accountRepository.getCountryId().firstOrNull()

		BaseResponse<Pair<List<Country>, List<Country>>?>(
			response.data?.firstOrNull { it.id?.toString() == countryId }?.let {
				it.cities.orEmpty() to it.areas.orEmpty()
			},
			response.message,
			response.code
		)
	}

}