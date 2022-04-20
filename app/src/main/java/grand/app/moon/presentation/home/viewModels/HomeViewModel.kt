package grand.app.moon.presentation.home.viewModels

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsHomeAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.home.HomeFragmentDirections
import grand.app.moon.presentation.story.adapter.StoriesAdapter
import grand.app.moon.presentation.store.adapter.StoreAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

  var storeUseCase: StoreUseCase,
  var userLocalUseCase: UserLocalUseCase,
  val accountRepository: AccountRepository,
  private val homeUseCase: HomeUseCase
) : BaseViewModel() {

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomeResponse>>>(Resource.Default)
  val homeResponse = _homeResponse

  private val _storiesResponse =
    MutableStateFlow<Resource<BaseResponse<ArrayList<Store>>>>(Resource.Default)
  val storiesResponse = _storiesResponse


  var titleToolbar = MutableLiveData<String>("")

  @Bindable
  val storiesAdapter = StoriesAdapter()

  @Bindable
  val categoriesAdapter = CategoriesAdapter()

  @Bindable
  val storeAdapter = StoreAdapter()

  @Bindable
  val adsHomeAdapter = AdsHomeAdapter()

  var isLoggin = userLocalUseCase.isLoggin()

  fun initAllServices() {
    storeAdapter.percentage = 48
    storeAdapter.isLogin = isLoggin
    storeAdapter.useCase = storeUseCase
    categoriesAdapter.percentage = 33
    getCategories()
    homeApi()
    getStories()
  }

  private fun getCategories() {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        categoriesAdapter.differ.submitList(it.data)
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }

  fun stores(v: View) {
    v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStoreListFragment(v.resources.getString(R.string.top_stores_rated)))
  }

  private fun homeApi() {
    homeUseCase.home()
      .onEach { result ->
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  private fun getStories() {
    homeUseCase.getStories(null)
      .onEach { result ->
        storiesResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun updateStories(data: MutableList<Store>) {
    storiesAdapter.differ.submitList(data)
    notifyPropertyChanged(BR.storiesAdapter)
  }

  fun departments() {
    clickEvent.value = Constants.DEPARTMENTS
  }

  fun search(v: View){
    v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
  }

  fun homeFilter(v: View){
  }

  fun departments(v: View) {
    v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDepartmentListFragment())
  }
  fun notification(v: View) {
    clickEvent.value = Constants.NOTIFICATION
  }
  fun notificationFilter(v: View) {
    clickEvent.value = Constants.NOTIFICATION_FILTER
  }


  fun chatList(v: View) {
    if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      clickEvent.value = Constants.CHAT_LIST
    }
  }

  private val TAG = "HomeViewModel"
  fun updateList(data: HomeResponse) {
    storeAdapter.differ.submitList(data.mostRatedStores)
    notifyPropertyChanged(BR.storeAdapter)

    Log.d(TAG, "updateList: " + data.categoryAds.size)
    adsHomeAdapter.differ.submitList(data.categoryAds)
    notifyPropertyChanged(BR.adsHomeAdapter)
    show.set(true)
  }
}