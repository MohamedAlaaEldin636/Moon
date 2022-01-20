package com.structure.base_mvvm.presentation.terms.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.settings.models.SettingsData
import com.structure.base_mvvm.domain.settings.use_case.SettingsUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  private val _settingsResponse =
    MutableStateFlow<Resource<BaseResponse<SettingsData>>>(Resource.Default)
  val settingsResponse = _settingsResponse
  @Bindable
  var settingsData = SettingsData()
    set(value) {
      field = value
      notifyPropertyChanged(BR.settingsData)
    }

  init {
    about()
  }

  fun about() {
    settingsUseCase(Constants.TERMS_TYPE, Constants.STUDENT_TYPE)
      .onEach { result ->
        _settingsResponse.value = result
      }
      .launchIn(viewModelScope)
  }
}