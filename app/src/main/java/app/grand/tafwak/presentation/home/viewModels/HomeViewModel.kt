package app.grand.tafwak.presentation.home.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import app.grand.tafwak.domain.account.use_case.UserLocalUseCase
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.home.use_case.HomeUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.BR
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.home.adapters.GroupsAdapter
import app.grand.tafwak.presentation.home.adapters.HomeSliderAdapter
import app.grand.tafwak.presentation.home.adapters.TeacherAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val homeUseCase: HomeUseCase,
  private val userLocalUseCase: UserLocalUseCase
) : BaseViewModel() {

  @Bindable
  val adapter: TeacherAdapter = TeacherAdapter()

  @Bindable
  val groupsAdapter: GroupsAdapter = GroupsAdapter()

  val homeSliderAdapter: HomeSliderAdapter = HomeSliderAdapter()

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomeStudentData>>>(Resource.Default)
  val homeResponse = _homeResponse

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
    getHomeStudent()
  }

  private fun getHomeStudent() {
    homeUseCase.getHomeDataLocal()
      .onEach { result ->
        println(result.toString())
//        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
//    viewModelScope.launch {
//      userLocalUseCase.getToken().collect {
//        Log.e("getHomeStudent", "getHomeStudent: "+it)
//      }
//    }

  }

  var homeStudentData: HomeStudentData = HomeStudentData()
    set(value) {
      adapter.differ.submitList(value.instructors)
      notifyPropertyChanged(BR.adapter)
      groupsAdapter.differ.submitList(value.classes)
      notifyPropertyChanged(BR.groupsAdapter)
      homeSliderAdapter.update(value.sliders)
      field = value
    }
}