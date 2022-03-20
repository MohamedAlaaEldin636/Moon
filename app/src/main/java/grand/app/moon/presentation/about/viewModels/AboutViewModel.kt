package grand.app.moon.presentation.about.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import com.structure.base_mvvm.BR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
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
    settingsUseCase(Constants.ABOUT_TYPE, Constants.APP_TYPE_GENERAL)
      .onEach { result ->
        _settingsResponse.value = result
      }
      .launchIn(viewModelScope)
  }
}