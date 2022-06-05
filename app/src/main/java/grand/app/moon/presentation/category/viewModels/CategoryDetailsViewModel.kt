package grand.app.moon.presentation.category.viewModels

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.CategoryDetails
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsHomeAdapter
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.category.view.CategoryDetailsFragmentDirections
import grand.app.moon.presentation.home.HomeFragmentDirections
import grand.app.moon.presentation.store.adapter.StoreAdapter
import grand.app.moon.presentation.story.adapter.StoriesAdapter
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

  private val _storiesResponse =
    MutableStateFlow<Resource<BaseResponse<ArrayList<Store>>>>(Resource.Default)
  val storiesResponse = _storiesResponse

  var categoryId: Int = -1
  var title = ObservableField<String>("")


  @Bindable
  val storiesAdapter = StoriesAdapter()


  @Bindable
  val categoriesAdapter = CategoriesAdapter()

  @Bindable
  val storeAdapter = StoreAdapter()

  @Inject
  @Bindable
  lateinit var adsHomeAdapter : AdsHomeAdapter

  val followStoreRequest = FollowStoreRequest()


  var isLoggin = userLocalUseCase.isLoggin()

  init {
    storeAdapter.percentage = 48
    storeAdapter.useCase = storeUseCase
    storeAdapter.isLogin = isLoggin
  }

  fun initAllServices() {
    categoriesAdapter.percentage = 33
    getCategoryDetails()
    getStories()
  }

  private fun getStories() {
    homeUseCase.getStories(categoryId)
      .onEach { result ->
        storiesResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  val categoryItem = CategoryItem(name = "", subCategories = arrayListOf(), total = 0)

  private fun getCategories() {
    categoryItem.id = categoryId
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach {
          if (it.id == categoryId) {
            it.subCategories?.forEach {
              it.categoryId = categoryId
            }
            it.subCategories?.add(0, categoryItem)
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

  fun search(v: View) {
    val action = CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToSearchFragment()
    action.categoryId = categoryId
    v.findNavController().navigate(action)
  }

  fun updateStories(data: MutableList<Store>) {
    storiesAdapter.differ.submitList(data)
    notifyPropertyChanged(BR.storiesAdapter)
  }

  fun setData(data: CategoryDetails, categoryAdvertisement: CategoryAdvertisement) {
    getCategories()
    storeAdapter.differ.submitList(data.stores)
    ListHelper.addFollowStore(data.stores)
    notifyPropertyChanged(BR.storeAdapter)

    val list = ArrayList<CategoryAdvertisement>()
    list.add(categoryAdvertisement)
    list[0].showMore.categoryId = categoryId
    if (list[0].advertisements.isNotEmpty()) {
      adsHomeAdapter.differ.submitList(list)
      notifyPropertyChanged(BR.adsHomeAdapter)
    }
  }

  fun follow() {
    followStoreRequest.storeId = storeAdapter.differ.currentList[storeAdapter.position].id
    storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
    storeAdapter.changeFollowingText()
  }

}