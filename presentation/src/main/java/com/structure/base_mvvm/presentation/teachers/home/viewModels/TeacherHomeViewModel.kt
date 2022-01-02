package com.structure.base_mvvm.presentation.teachers.home.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.home.models.HomePaginateData
import com.structure.base_mvvm.domain.home.use_case.HomeUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import com.structure.base_mvvm.presentation.teachers.adapters.SubjectsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : BaseViewModel() {

  val showPrettyPopUp = SingleLiveEvent<Void>()

  @Bindable
  val adapter: SubjectsAdapter = SubjectsAdapter()

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomePaginateData>>>(Resource.Default)
  val homeResponse = _homeResponse

  init {
//    getHome(1, true)
  }

  fun onShowPrettyPopUpClicked() {
    showPrettyPopUp.call()
  }

  fun getHome(page: Int, showProgress: Boolean) {
    homeUseCase.getHome(page, showProgress)
      .onEach { result ->
        println(result.toString())
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  var homePaginateData: HomePaginateData = HomePaginateData()
    set(value) {
      Log.e("homePaginateData", ": " + value.list.size)
      adapter.differ.submitList(value.list)
      notifyPropertyChanged(BR.adapter)
      field = value
    }
}