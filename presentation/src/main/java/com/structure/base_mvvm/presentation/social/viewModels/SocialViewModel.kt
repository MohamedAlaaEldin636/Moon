package com.structure.base_mvvm.presentation.social.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.settings.models.SettingsData
import com.structure.base_mvvm.domain.settings.use_case.SettingsUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.social.adapters.SocialAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  @Bindable
  val adapter: SocialAdapter = SocialAdapter()
  private val _socialResponse =
    MutableStateFlow<Resource<BaseResponse<List<SettingsData>>>>(Resource.Default)
  val socialResponse = _socialResponse

  init {
    getSocial()
  }

  private fun getSocial() {
    settingsUseCase(Constants.SOCIAL_TYPE)
      .onEach { result ->
        println(result.toString())
        _socialResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun updateSocial(socialList: List<SettingsData>) {
    adapter.differ.submitList(socialList)
    notifyPropertyChanged(BR.adapter)
  }
}