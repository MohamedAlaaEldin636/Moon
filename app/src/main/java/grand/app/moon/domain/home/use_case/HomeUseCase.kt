package grand.app.moon.domain.home.use_case

import android.util.Log
import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.home.repository.HomeRepository
import grand.app.moon.domain.home.repository.local.HomeLocalRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class HomeUseCase @Inject constructor(
  private val homeRepository: HomeRepository,
  private val homeLocalRepository: HomeLocalRepository,
) {
<<<<<<< HEAD
//  operator fun invoke(): Flow<Resource<BaseResponse<HomeStudentData>>> =
//    flow {
//
//      Log.e("invoke", "invoke: ")
//      val result = homeRepository.studentHome()
////      if (result is Resource.Success) {
////        homeLocalRepository.insertStudentHomeLocal(result.value.data)
////      }
//
//      emit(result)
//    }.flowOn(Dispatchers.IO)
//
//  fun getHomeDataLocal(): Flow<HomeStudentData> =
//    flow {
//      homeLocalRepository.studentHomeLocal().collect { homeStudent ->
//        Log.e("getHomeDataLocal", "getHomeDataLocal: ")
//        if (homeStudent != null) {
//          Log.e("getHomeDataLocal", "getHomeDataLocal: " + homeStudent)
//          emit(homeStudent)
//        } else {
//          Log.e("getHomeDataLocal", "getHomeDataLocal: INVOKE")
//          invoke()
//        }
//      }
//    }.flowOn(Dispatchers.IO)



  var showLoader = false
=======
>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
  operator fun invoke(): Flow<HomeStudentData> =
    flow {
      homeLocalRepository.studentHomeLocal().collect { homeStudent ->
        Log.e("getHomeDataLocal", "getHomeDataLocal: ")
<<<<<<< HEAD
        if (homeStudent != null) {
          showLoader = false
          Log.e("getHomeDataLocal", "getHomeDataLocal: " + homeStudent)
          emit(homeStudent)
        } else {
          showLoader = true
        }
        Log.d("invoke", "invoke:invoke ")
      }
    }.flowOn(Dispatchers.IO)
  val myFlow = MutableSharedFlow<Resource<BaseResponse<HomeStudentData>>>()

  private suspend fun remoteService() {
    Log.e("invoke", "invoke: starting")
//    flow {
//
//
//    }.flowOn(Dispatchers.IO)
    Log.e("invoke", "invoke: ")
    val result = homeRepository.studentHome()
    if (showLoader) myFlow.emit(result)
    if (result is Resource.Success) {
      if (showLoader) myFlow.emit(Resource.HideLoading)
      homeLocalRepository.deleteAll()
      homeLocalRepository.insertStudentHomeLocal(result.value.data)
    }
  }
=======
//        if (homeStudent != null) {
        Log.e("getHomeDataLocal", "getHomeDataLocal: " + homeStudent)
        emit(homeStudent)
//        }
      }
    }.flowOn(Dispatchers.IO)

  fun homeService(): Flow<Resource<BaseResponse<HomeStudentData>>> = flow {
    Log.e("invoke", "invoke: starting")
    val result = homeRepository.studentHome()
    if (result is Resource.Success) {
      homeLocalRepository.deleteAll()
      homeLocalRepository.insertStudentHomeLocal(result.value.data)
    }
    emit(result)
  }.flowOn(Dispatchers.IO)

>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
}
