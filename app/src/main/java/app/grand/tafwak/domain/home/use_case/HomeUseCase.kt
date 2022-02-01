package app.grand.tafwak.domain.home.use_case

import android.util.Log
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
  private val homeLocalRepository: HomeLocalRepository,
) {
  operator fun invoke(): Flow<Resource<BaseResponse<HomeStudentData>>> =
    flow {

      Log.e("invoke", "invoke: ")
      val result = homeRepository.studentHome()
//      if (result is Resource.Success) {
//        homeLocalRepository.insertStudentHomeLocal(result.value.data)
//      }

      emit(result)
    }.flowOn(Dispatchers.IO)

  fun getHomeDataLocal(): Flow<HomeStudentData> =
    flow {
      homeLocalRepository.studentHomeLocal().collect { homeStudent ->
        Log.e("getHomeDataLocal", "getHomeDataLocal: ")
        if (homeStudent != null) {
          Log.e("getHomeDataLocal", "getHomeDataLocal: " + homeStudent)
          emit(homeStudent)
        } else {
          Log.e("getHomeDataLocal", "getHomeDataLocal: INVOKE")
          invoke()
        }
      }
    }.flowOn(Dispatchers.IO)
}