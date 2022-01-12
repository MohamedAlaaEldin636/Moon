package com.structure.base_mvvm.presentation.auth.schoolInfo.grades.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.educational.entity.model.Stage
import com.structure.base_mvvm.domain.educational.use_case.EducationalUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.auth.schoolInfo.grades.adapters.StagesAdapter
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SchoolGradesViewModel @Inject constructor(private val educationalUseCase: EducationalUseCase) :
  BaseViewModel() {
  private val _stagesResponse =
    MutableStateFlow<Resource<BaseResponse<List<Stage>>>>(Resource.Default)
  val stagesResponse = _stagesResponse
  private val _registerResponse =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val registerResponse = _registerResponse

  @Bindable
  val adapter: StagesAdapter = StagesAdapter()

  init {
    getStages()
  }

  private fun getStages() {
    educationalUseCase.invoke()
      .onEach { result ->
        _stagesResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun registerStep3() {
    if (adapter.lastSelected != -1) {
      educationalUseCase.registerStep3(adapter.lastSelected)
        .onEach { result ->
          _registerResponse.value = result
        }
        .launchIn(viewModelScope)
    }
  }

  fun updateAdapter(stages: List<Stage>) {
    adapter.differ.submitList(stages)
    notifyPropertyChanged(BR.adapter)
  }

}