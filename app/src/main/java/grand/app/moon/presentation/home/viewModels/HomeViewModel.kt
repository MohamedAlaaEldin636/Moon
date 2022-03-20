package grand.app.moon.presentation.home.viewModels

import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val homeUseCase: HomeUseCase,
  private val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomeStudentData>>>(Resource.Default)
  val homeResponse = _homeResponse
  private val _homeCachResponse =
    MutableStateFlow<HomeStudentData>(HomeStudentData())
  val homeCachResponse = _homeCachResponse

  init {
//    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//      if (!task.isSuccessful) {
//        return@OnCompleteListener
//      }
//      var result = task.result
//      Log.e("setupFirebaseToken", "setupFirebaseToken: $result")
//      //shared perereference
//      viewModelScope.launch {
//        userLocalUseCase.saveToken(result)
//      }
//    })
    homeCached()
    getHomeStudent()
  }

<<<<<<< HEAD
  private  val TAG = "HomeViewModel"
  private fun getHomeStudent() {
    viewModelScope.launch {
      homeUseCase.invoke()
        .collect { result ->
          Log.d(TAG, "getHomeStudent: $result")
          println(result.toString())
//        _homeResponse.value = result
        }
    }


//    viewModelScope.launch {
//      userLocalUseCase.getToken().collect {
//        Log.e("getHomeStudent", "getHomeStudent: "+it)
//      }
//    }
=======
   private fun getHomeStudent() {
    homeUseCase.homeService()
      .onEach { result ->
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
////    viewModelScope.launch {
////      userLocalUseCase.getToken().collect {
////        Log.e("getHomeStudent", "getHomeStudent: "+it)
////      }
////    }
//
>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a

  }

 private fun homeCached() {
    viewModelScope.launch {
      homeUseCase.invoke()
        .collect { result ->
          _homeCachResponse.value = result
        }
    }
  }

  var homeStudentData: HomeStudentData = HomeStudentData()
    set(value) {
//      adapter.differ.submitList(value.instructors)
      notifyPropertyChanged(BR.adapter)
//      groupsAdapter.differ.submitList(value.classes)
//      notifyPropertyChanged(BR.groupsAdapter)
//      homeSliderAdapter.update(value.sliders)
      field = value
    }
}