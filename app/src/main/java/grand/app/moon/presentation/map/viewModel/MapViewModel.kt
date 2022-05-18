package grand.app.moon.presentation.map.viewModel

import androidx.annotation.NonNull
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
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
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
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MapViewModel @Inject constructor(
  private val mapUseCase: MapUseCase,
  val accountRepository: AccountRepository,
) : BaseViewModel() {
  var mapConfig: MapConfig? = null
  val clusterCustomItems = arrayListOf<ClusterCustomItem>()

  //  var backUp = arrayListOf<Store>()
  var stores = arrayListOf<Store>()
  var latLngs = ArrayList<LatLng>()
  var markerRender: MarkerRender? = null
  var type: String = Constants.STORE
  var propertyId: String? = null
  var subCategoryId: String? = null
  var categoryId: String? = null

  val advertisement = ObservableField<Advertisement>(Advertisement())
  val showAdvertisement = ObservableBoolean(false)

  var categoriesAdapter = MapCategoriesAdapter()
  val responseStores = MutableStateFlow<Resource<BaseResponse<List<Store>>>>(Resource.Default)
  val responseAds = MutableStateFlow<Resource<BaseResponse<List<Advertisement>>>>(Resource.Default)
  val categoryItem =
    CategoryItem(-1, name = "", subCategories = arrayListOf(), image = "", total = 0)

  fun getCategories() {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.add(0, categoryItem)
        categoriesAdapter.selected = 0
        categoriesAdapter.differ.submitList(it.data)
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }

  fun setData(data: List<Store>) {
//    backUp.addAll(data)
    stores.addAll(data)
  }

  fun setAdsData(data: List<Advertisement>) {
    data.forEach {
//      backUp.add(
//        Store(
//          id = it.id,
//          latitude = it.latitude,
//          longitude = it.longitude,
//          nickname = it.price.toString() + it.country.currency,
//          subCategoryId = it.subCategoryId
//        )
//      )
      stores.add(
        Store(
          id = it.id,
          latitude = it.latitude,
          longitude = it.longitude,
          nickname = it.price.toString() + "\n" + it.country.currency,
          subCategoryId = it.subCategoryId
        )
      )
    }
  }

  private val TAG = "MapViewModel"
  fun callService() {
    when (type) {
      Constants.ADVERTISEMENT_TEXT -> {
        if (categoriesAdapter.differ.currentList.isNotEmpty()) {
          val tmp = categoriesAdapter.differ.currentList[categoriesAdapter.selected].id
          propertyId =
            when (tmp) {
              -1 -> null
              else -> tmp.toString()
            }
          categoriesAdapter.differ.currentList[categoriesAdapter.selected].id.toString()
        }
        mapUseCase.mapAds(type, categoryId, subCategoryId, propertyId)
          .onEach { result ->
            responseAds.value = result
          }
          .launchIn(viewModelScope)
      }
      else -> {
        if (categoriesAdapter.differ.currentList.isNotEmpty()) {
          val tmp = categoriesAdapter.differ.currentList[categoriesAdapter.selected].id
          categoryId =
            when (tmp) {
              -1 -> null
              else -> tmp.toString()
            }
          categoriesAdapter.differ.currentList[categoriesAdapter.selected].id.toString()
        }
        mapUseCase.mapStore(type, categoryId, null, null)
          .onEach { result ->
            responseStores.value = result
          }
          .launchIn(viewModelScope)
      }
    }
  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun setSubCategories(subCategory: SubCategoryResponse) {
    val categoryItems = ArrayList<CategoryItem>()
    subCategory.properties.forEach {
      categoryItems.add(
        CategoryItem(
          id = it.id,
          name = it.name,
          subCategories = arrayListOf(),
          image = "",
          total = 0
        )
      )
    }
    categoryItems.add(0, categoryItem)
    categoriesAdapter.selected = 0
    categoriesAdapter.differ.submitList(categoryItems)
    notifyPropertyChanged(BR.categoriesAdapter)
  }


}