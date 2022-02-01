package app.grand.tafwak.presentation.teachers.home.viewModels

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.home.models.HomePaginateData
import app.grand.tafwak.domain.home.use_case.HomeUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.BR
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import app.grand.tafwak.presentation.teachers.adapters.SubjectsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : BaseViewModel() {

  val showPrettyPopUp = SingleLiveEvent<Void>()

  @Bindable
  val adapter: SubjectsAdapter = SubjectsAdapter()

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomePaginateData>>>(Resource.Default)
  val homeResponse = _homeResponse

  init {
//    getHome(1, true)
  }

  fun onShowPrettyPopUpClicked() {
    showPrettyPopUp.call()
  }

  fun getHome(page: Int, showProgress: Boolean) {
    homeUseCase.invoke()
      .onEach { result ->
        println(result.toString())
//        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  var homePaginateData: HomePaginateData = HomePaginateData()
    set(value) {
      Log.e("homePaginateData", ": " + value.list.size)
//      adapter.differ.submitList(value.list)
      notifyPropertyChanged(BR.adapter)
      field = value
    }
}