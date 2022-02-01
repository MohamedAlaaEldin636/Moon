package app.grand.tafwak.presentation.contactUs.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.settings.models.ContactUsRequest
import app.grand.tafwak.domain.settings.use_case.SettingsUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
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