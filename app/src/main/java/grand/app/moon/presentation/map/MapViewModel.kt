package grand.app.moon.presentation.map

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.map.use_case.MapUseCase
import grand.app.moon.domain.settings.entity.NotificationPaginateData
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.helpers.map.cluster.ClusterCustomItem
import grand.app.moon.helpers.map.cluster.MarkerRender
import grand.app.moon.presentation.more.MoreAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
  private val mapUseCase: MapUseCase
) : BaseViewModel() {
  lateinit var mapConfig: MapConfig
  val clusterCustomItems = arrayListOf<ClusterCustomItem>()
  var stores = arrayListOf<Store>()
  var latLngs = ArrayList<LatLng>()
  var markerRender : MarkerRender? = null
  val showProgress = ObservableBoolean(false)
  var type: String = "store"

  val _responseService = MutableStateFlow<Resource<BaseResponse<List<Store>>>>(Resource.Default)

  val response = _responseService

  fun setData(data: List<Store>) {
    stores.addAll(data)
  }

  fun callService() {
//    job = mapUseCase.map(type).onEach {
//      _responseService.value = it
//      when{
//        it is Resource.Loading ->  showProgress.set(false)
//        it is Resource.Success -> {
//          setData(it.value.data)
//        }
//      }
//    }.launchIn(viewModelScope)

    mapUseCase.map(type)
      .onEach { result ->
        _responseService.value = result
      }
      .launchIn(viewModelScope)

  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }
}