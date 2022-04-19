package grand.app.moon.presentation.store.viewModels

import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.store.adapter.ReportAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class ReportViewModel @Inject constructor(
  val useCase: SettingsUseCase,
) : BaseViewModel() {

  val progress = ObservableBoolean(false)
  val title = ObservableField("")
  @Bindable
  val adapter = ReportAdapter()

  val _response =
    MutableStateFlow<Resource<BaseResponse<List<AppTutorial>>>>(Resource.Default)
  val response = _response



  fun callService() {
    progress.set(true)
    useCase.onBoard("8")
      .onEach {
        response.value = it
      }.launchIn(viewModelScope)
  }

  fun submitService() {
//    progress.set(true)
//    useCase.onBoard("8")
//      .onEach {
//        response.value = it
//      }.launchIn(viewModelScope)
  }

  fun setData(data: List<AppTutorial>) {
    adapter.differ.submitList(data)
    notifyPropertyChanged(BR.adapter)
  }
}