package grand.app.moon.presentation.ads.viewModels

import android.util.Log
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
class FilterDialogViewModel @Inject constructor(
) : BaseViewModel() {

  val progress = ObservableBoolean(false)
  val title = ObservableField("")
  @Bindable
  val adapter = ReportAdapter()
  val list = arrayListOf<AppTutorial>()

  init {
  }

  fun setData(idSelected : Int) {
    list.forEachIndexed{ index, data ->
      if(data.id == idSelected){
        adapter.lastPosition = index
        adapter.lastSelected = idSelected
      }
    }
    adapter.differ.submitList(list)
    notifyPropertyChanged(BR.adapter)
  }
}