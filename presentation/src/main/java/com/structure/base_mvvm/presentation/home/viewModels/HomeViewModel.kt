package com.structure.base_mvvm.presentation.home.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.structure.base_mvvm.domain.account.repository.AccountRepository
import com.structure.base_mvvm.domain.account.use_case.UserLocalUseCase
import com.structure.base_mvvm.domain.home.models.HomeStudentData
import com.structure.base_mvvm.domain.home.use_case.HomeUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.home.adapters.GroupsAdapter
import com.structure.base_mvvm.presentation.home.adapters.HomeSliderAdapter
import com.structure.base_mvvm.presentation.home.adapters.TeacherAdapter
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
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
      if (!task.isSuccessful) {
        return@OnCompleteListener
      }
      var result = task.result
      Log.e("setupFirebaseToken", "setupFirebaseToken: $result")
      //shared perereference
      viewModelScope.launch {
        userLocalUseCase.saveToken(result)
      }
    })
    getHomeStudent()
  }

  private fun getHomeStudent() {
    homeUseCase.invoke()
      .onEach { result ->
        println(result.toString())
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
    viewModelScope.launch {
      userLocalUseCase.getToken().collect {
        Log.e("getHomeStudent", "getHomeStudent: "+it)
      }
    }
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