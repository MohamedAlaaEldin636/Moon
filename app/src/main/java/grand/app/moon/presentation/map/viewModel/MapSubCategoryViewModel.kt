package grand.app.moon.presentation.map.viewModel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.map.use_case.MapUseCase
import grand.app.moon.domain.subCategory.entity.Property
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.helpers.map.MapConfig
import grand.app.moon.helpers.map.cluster.ClusterCustomItem
import grand.app.moon.helpers.map.cluster.MarkerRender
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.map.MapCategoriesAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.annotation.Nullable
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MapSubCategoryViewModel @Inject constructor(
  private val mapUseCase: MapUseCase,
  val accountRepository: AccountRepository,
) : BaseViewModel() {
  lateinit var mapConfig: MapConfig
  val clusterCustomItems = arrayListOf<ClusterCustomItem>()
  var backUp = arrayListOf<Store>()
  var stores = arrayListOf<Store>()
  var latLngs = ArrayList<LatLng>()
  var markerRender: MarkerRender? = null
  var type: String = Constants.ADVERTISEMENT_TEXT
  var propertyId: String? = null
  var subCategoryId: String? = null

  val advertisement = ObservableField<Advertisement>(Advertisement())
  val showAdvertisement = ObservableBoolean(false)

  var categoriesAdapter = MapCategoriesAdapter()
  val responseAds = MutableStateFlow<Resource<BaseResponse<List<Advertisement>>>>(Resource.Default)
  val categoryItem =
    CategoryItem(-1, name = "", subCategories = arrayListOf(), image = "", total = 0)

  fun setProperties(properties: ArrayList<Property>) {
    val list = arrayListOf<CategoryItem>()
    properties.forEach {
      list.add(
        CategoryItem(
          it.id,
          name = it.name,
          subCategories = arrayListOf(),
          image = "",
          total = 0
        )
      )
    }
    list.add(0, categoryItem)
    categoriesAdapter.selected = 0
    categoriesAdapter.differ.submitList(list)
    notifyPropertyChanged(BR.categoriesAdapter)
  }

  var listAds = ArrayList<Advertisement>()
  fun setDataAds(data: List<Advertisement>) {
    listAds.addAll(data)
    data.forEach {
      val store = Store()
      store.id = it.id
      store.image = it.image
      store.nickname = it.title
      store.latitude = it.latitude
      store.longitude = it.longitude
      store.type = Constants.ADVERTISEMENT_TEXT
//      store.category.add(CategoryItem(id = it.categoryId,"","", arrayListOf(),null))
      stores.add(store)
    }
    backUp.addAll(stores)
  }

  fun setSelectedAds(id: Int) {
    listAds.forEach {
      if (it.id == id) {
        advertisement.set(it)
        showAdvertisement.set(true)
      }
    }
  }

  fun setData(data: List<Store>) {
    backUp.addAll(data)
    stores.addAll(data)
  }

  private val TAG = "MapViewModel"
  fun callService() {
    Log.d(TAG, "callService: $type")
    mapUseCase.mapAds(type, propertyId, subCategoryId)
      .onEach { result ->
        responseAds.value = result
      }
      .launchIn(viewModelScope)
  }

  fun whatsapp(v: View) {
    advertisement.get()?.store?.phone?.let {
      shareWhatsapp(
        v, advertisement.get()!!.title, advertisement.get()!!.description,
        it
      )
    }
  }


  fun phone(v: View) {
    advertisement.get()?.store?.phone?.let { callPhone(v.context, it) }
  }

  //take-care
  fun chat(v: View) {

  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }
}