package grand.app.moon.presentation.contactUs.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase,
  private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
  var request = ContactUsRequest()
  private val _contactResponse = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val contactResponse = _contactResponse

  var validationException = SingleLiveEvent<Int>()

  init {
    savedStateHandle.get<String>("type")?.let { type ->
      request.type = type
    }
  }

  fun onContactClicked() {
    settingsUseCase(request)
      .catch { exception -> validationException.value = exception.message?.toInt() }
      .onEach { result ->
        _contactResponse.value = result
      }
      .launchIn(viewModelScope)
  }
}