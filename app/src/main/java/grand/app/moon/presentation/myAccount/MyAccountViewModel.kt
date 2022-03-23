package grand.app.moon.presentation.myAccount

import androidx.databinding.Bindable
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.presentation.more.MoreAdapter
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  @Bindable
  val moreAdapter = MoreAdapter()
}