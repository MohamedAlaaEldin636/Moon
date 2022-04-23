package grand.app.moon.presentation.media.image

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.explore.adapter.ExploreAdapter
import grand.app.moon.presentation.more.MoreAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ZoomViewModel @Inject constructor(
  private val useCase: ExploreUseCase,
) : BaseViewModel() {
  val image = ObservableField("")
}