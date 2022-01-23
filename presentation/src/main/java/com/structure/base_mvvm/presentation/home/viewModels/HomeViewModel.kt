package com.structure.base_mvvm.presentation.home.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val homeUseCase: HomeUseCase
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
    getHomeStudent()
  }

  private fun getHomeStudent() {
    homeUseCase.invoke()
      .onEach { result ->
        println(result.toString())
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
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