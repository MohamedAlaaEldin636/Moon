package grand.app.moon.presentation.social.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.BR
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.social.adapters.SocialAdapter
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