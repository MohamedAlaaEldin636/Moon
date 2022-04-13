package grand.app.moon.presentation.map.viewModel

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.map.use_case.MapUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.helpers.map.cluster.ClusterCustomItem
import grand.app.moon.helpers.map.cluster.MarkerRender
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.map.MapCategoriesAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
  private val mapUseCase: MapUseCase,
  val accountRepository: AccountRepository,
  ) : BaseViewModel() {
  lateinit var mapConfig: MapConfig
  val clusterCustomItems = arrayListOf<ClusterCustomItem>()
  var stores = arrayListOf<Store>()
  var latLngs = ArrayList<LatLng>()
  var markerRender : MarkerRender? = null
  var type: String = "store"
  var categoriesAdapter  = MapCategoriesAdapter()
  val response = MutableStateFlow<Resource<BaseResponse<List<Store>>>>(Resource.Default)
  val categoryItem = CategoryItem(-1,name="",subCategories = arrayListOf(),image = "",total = 0)

  fun getCategories() {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.add(0,categoryItem)
        categoriesAdapter.selected = 0
        categoriesAdapter.differ.submitList(it.data)
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }


  fun setData(data: List<Store>) {
    stores.addAll(data)
  }



  fun callService() {
    mapUseCase.map(type)
      .onEach { result ->
        response.value = result
      }
      .launchIn(viewModelScope)

  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }
}