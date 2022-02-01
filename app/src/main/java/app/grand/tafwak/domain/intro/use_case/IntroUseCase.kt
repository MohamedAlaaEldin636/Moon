package app.grand.tafwak.domain.intro.use_case

import app.grand.tafwak.domain.intro.entity.AppTutorial
import app.grand.tafwak.domain.intro.repository.IntroRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class IntroUseCase @Inject constructor(
  private val introRepository: IntroRepository) {

  operator fun invoke(): Flow<Resource<BaseResponse<List<AppTutorial>>>> = flow {
    emit(Resource.Loading)
    val result = introRepository.intro()
    emit(result)
  }.flowOn(Dispatchers.IO)
}