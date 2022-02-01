package app.grand.tafwak.domain.home.use_case

import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.home.repository.HomeRepository
import app.grand.tafwak.domain.home.repository.local.HomeLocalRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class HomeUseCase @Inject constructor(
  private val homeRepository: HomeRepository,
//  private val homeLocalRepository: HomeLocalRepository,
) {
  operator fun invoke(): Flow<Resource<BaseResponse<HomeStudentData>>> =
    flow {
      emit(Resource.Loading)
      val result = homeRepository.studentHome()

      emit(result)
    }.flowOn(Dispatchers.IO)
//  private val homeLocalRepository: HomeLocalRepository

//  fun getHomeDataLocal(): Flow<HomeStudentData> =
//    flow {
//      homeLocalRepository.studentHomeLocal().collect {
//        emit(it)
//      }
//    }.flowOn(Dispatchers.IO)
}