package grand.app.moon.presentation.social.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.presentation.notfication.adapter.NotificationAdapter
import grand.app.moon.presentation.social.SocialAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  @Bindable
//  val adapter: SocialAdapter = SocialAdapter()
  private val _socialResponse =
    MutableStateFlow<Resource<BaseResponse<List<SettingsData>>>>(Resource.Default)
  val response = _socialResponse

  @Bindable
  var adapter = SocialAdapter()

  init {
    getSocial()
  }

  private fun getSocial() {
    settingsUseCase.settings("3")
      .onEach { result ->
        response.value = result
      }
      .launchIn(viewModelScope)
  }


  fun setData(data: List<SettingsData>) {
    adapter.insertData(data)
    notifyPropertyChanged(BR.adapter)
  }
}