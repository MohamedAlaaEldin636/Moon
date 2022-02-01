package app.grand.tafwak.presentation.social.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.settings.models.SettingsData
import app.grand.tafwak.domain.settings.use_case.SettingsUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Constants
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.BR
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.social.adapters.SocialAdapter
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