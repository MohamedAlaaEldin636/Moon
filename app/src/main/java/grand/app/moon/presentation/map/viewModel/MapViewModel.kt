package grand.app.moon.presentation.map.viewModel

import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.InteractionRequest
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
import kotlinx.coroutines.Dispatchers
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
  val adsRepository: AdsRepository,
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
  val all_ads = ArrayList<Advertisement>()
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

  fun details(v: View) {
    Log.d(TAG, "details_id: ${advertisement.get()?.id}")
    Log.d(TAG, "details_type: $type")
//    v.findNavController().navigate(
//      R.id.nav_ads, bundleOf(
//        "id" to advertisement.get()?.id,
//        "type" to type
//      )
//    )
  }


  fun setAdsData(data: List<Advertisement>) {
    all_ads.addAll(data)
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

  fun findAds(id: Int) {
    all_ads.forEach {
      if (it.id == id) {
        this.advertisement.set(it)
        showAdvertisement.set(true)
      }
    }
  }

  fun whatsapp(v: View) {
    advertisement.get()?.let {
      viewModelScope.launch(Dispatchers.IO) {
        adsRepository.setInteraction(InteractionRequest(it.id.toString(), 7))
      }
      shareWhatsapp(v, it.title, it.description, it.country.country_code + it.phone)
    }
  }

  fun phone(v: View) {
    advertisement.get()?.let {
      viewModelScope.launch(Dispatchers.IO) {
        adsRepository.setInteraction(InteractionRequest(it.id.toString(), 6))
      }
      advertisement.get()?.let {
        callPhone(v.context, it.country.country_code + it.phone)
      }
    }

  }

  //take-care
  fun chat(v: View) {
    advertisement.get()?.let {
      if (v.context.isLoginWithOpenAuth()) {
        viewModelScope.launch(Dispatchers.IO) {
          adsRepository.setInteraction(InteractionRequest(it.id.toString(), 8))
        }
        it.store?.let { store ->
          v.context.openChatStore(v, store.id, store.name, store.image)
        }
      }
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