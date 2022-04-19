package grand.app.moon.presentation.category.viewModels

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsHomeAdapter
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.store.adapter.StoreAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
  var storeUseCase: StoreUseCase,
  var userLocalUseCase: UserLocalUseCase,
  val accountRepository: AccountRepository,
  private val homeUseCase: HomeUseCase
) : BaseViewModel() {

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<CategoryDetails>>>(Resource.Default)
  val homeResponse = _homeResponse

  var categoryId: Int = -1
  var title = ObservableField<String>("")


  @Bindable
  val categoriesAdapter = CategoriesAdapter()

  @Bindable
  val storeAdapter = StoreAdapter()

  @Bindable
  val adsHomeAdapter = AdsHomeAdapter()

  var isLoggin = userLocalUseCase.isLoggin()

  fun initAllServices() {
    storeAdapter.percentage = 48
    categoriesAdapter.percentage = 33
    getCategoryDetails()
  }
  val categoryItem = CategoryItem(name = "",subCategories = arrayListOf(),total = 0)

  private fun getCategories() {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach {
          if (it.id == categoryId) {
            it.subCategories?.add(0,categoryItem)
            categoriesAdapter.differ.submitList(it.subCategories)
          }
        }
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }

  private fun getCategoryDetails() {
    homeUseCase.getCategoryDetails(categoryId)
      .onEach { result ->
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun setData(data: CategoryDetails, categoryAdvertisement: CategoryAdvertisement) {
    getCategories()
    storeAdapter.differ.submitList(data.stores)
    notifyPropertyChanged(BR.storeAdapter)

    val list = ArrayList<CategoryAdvertisement>()
    list.add(categoryAdvertisement)
    list[0].showMore.categoryId = categoryId
    if (list[0].advertisements.isNotEmpty()) {
      adsHomeAdapter.differ.submitList(list)
      notifyPropertyChanged(BR.adsHomeAdapter)
    }
  }
}