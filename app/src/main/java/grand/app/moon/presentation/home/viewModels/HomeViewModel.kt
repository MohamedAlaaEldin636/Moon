package grand.app.moon.presentation.home.viewModels

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.ResponseAppGlobalAnnouncement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.addStore.BrowserHelper
import grand.app.moon.presentation.ads.adapter.AdsHomeAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.home.HomeFragmentDirections
import grand.app.moon.presentation.story.adapter.StoriesAdapter
import grand.app.moon.presentation.store.adapter.StoreAdapter
import grand.app.moon.presentation.story.adapter.StoriesAllAdapter
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

  var isRefresh = false
  val followStoreRequest = FollowStoreRequest()

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomeResponse>>>(Resource.Default)
  val homeResponse = _homeResponse

	private val _appGlobalResponse =
		MutableStateFlow<Resource<BaseResponse<ResponseAppGlobalAnnouncement?>>>(Resource.Default)
	val appGlobalResponse: Flow<Resource<BaseResponse<ResponseAppGlobalAnnouncement?>>> =
		_appGlobalResponse

	val showLoading = homeResponse.combine(appGlobalResponse) { first, second ->
		first is Resource.Loading || second is Resource.Loading
	}

  private val _storiesResponse =
    MutableStateFlow<Resource<BaseResponse<ArrayList<Store>>>>(Resource.Default)
  val storiesResponse = _storiesResponse


  val browserHelper = BrowserHelper()
  
  var titleToolbar = MutableLiveData<String>("")

  @Bindable
  val storiesAdapter = StoriesAdapter()

  @Bindable
  val storiesAllAdapter = StoriesAllAdapter()

  @Bindable
  val categoriesAdapter = CategoriesAdapter()

  @Bindable
  val storeAdapter = StoreAdapter()

  @Bindable
  val followingsStoresAdapter = StoreAdapter()


  @Inject
  @Bindable
  lateinit var adsHomeAdapter: AdsHomeAdapter

  var isLoggin = userLocalUseCase.isLoggin()

  init {
    initIsStoreUser()
  }

	fun getAppGlobalAnnouncement() {
		homeUseCase.getAppGlobalAnnouncement(true)
			.onEach { result ->
				_appGlobalResponse.value = result
			}
			.launchIn(viewModelScope)
	}

  fun initIsStoreUser() {
    val lastUrlStorage = accountRepository.getKeyFromLocal(Constants.LAST_URL)
    browserHelper.lastUrl = "https://souqmoon.com/store/login"
    if (lastUrlStorage.isNotEmpty() && !browserHelper.isUser(lastUrlStorage))
      browserHelper.lastUrl = lastUrlStorage
  }

  fun initAllServices() {
    storeAdapter.percentage = 51
    storeAdapter.isLogin = isLoggin
    storeAdapter.useCase = storeUseCase

    followingsStoresAdapter.percentage = 51
    followingsStoresAdapter.isLogin = isLoggin
    followingsStoresAdapter.useCase = storeUseCase
    followingsStoresAdapter.adapterType = Constants.FOLLOWED_STORES


    categoriesAdapter.percentage = 33
    getCategories()
    callService()
  }

  fun callService() {
    homeApi()
    getStories()
  }

  private fun getCategories() {
    accountRepository
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        val list = ArrayList<CategoryItem>()
        it.data.forEach {
          it.subCategories?.let { subCategory ->
            if (subCategory.size > 0)
              list.add(it)
          }
        }
        categoriesAdapter.differ.submitList(list)
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }

  fun stores(v: View) {
    v.findNavController().navigate(
      HomeFragmentDirections.actionHomeFragmentToStoreListFragment(
        v.resources.getString(R.string.top_stores_rated),
        3
      )
    )
  }

  fun departments(v: View) {
    v.findNavController()
      .navigate(HomeFragmentDirections.actionHomeFragmentToDepartmentListFragment())
  }

  private fun homeApi() {
    homeUseCase.home(isRefresh)
      .onEach { result ->
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun getStories() {
    Log.d(TAG, "getStories: WORKED")
    homeUseCase.getStories(null)
      .onEach { result ->
        storiesResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun updateStories(data: MutableList<Store>) {
    storiesAdapter.differ.submitList(null)
    storiesAdapter.differ.submitList(data)
    notifyPropertyChanged(BR.storiesAdapter)
  }

  fun followedStores(v: View){
    val uri = Uri.Builder()
      .scheme("store")
      .authority("grand.app.moon.store.followed")
      .build()
    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
    v.findNavController().navigate(request)
  }

  fun search(v: View) {
    v.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
  }

  fun homeFilter(v: View) {
    toFilter(v)
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

  fun goToMap(v: View) {
	  clickEvent.value = Constants.GO_TO_MAP
    /*if (!isLoggin) clickEvent.value = Constants.LOGIN_REQUIRED
    else {
      clickEvent.value = Constants.CHAT_LIST
    }*/
  }

  private val TAG = "HomeViewModel"
  fun updateList(data: HomeResponse) {
    storeAdapter.differ.submitList(data.mostRatedStores)
    followingsStoresAdapter.differ.submitList(data.followingsStores)
    notifyPropertyChanged(BR.followingsStoresAdapter)
    notifyPropertyChanged(BR.storeAdapter)

    val homeList = ArrayList<CategoryAdvertisement>()
    data.categoryAds.forEach {
      if (it.advertisements.size > 0) homeList.add(it)
    }
    homeList.forEach {
      ListHelper.addAllAds(it.advertisements)
    }

    ListHelper.addFollowStore(data.followingsStores)
    ListHelper.addFollowStore(data.mostRatedStores)

    adsHomeAdapter.differ.submitList(homeList)
    notifyPropertyChanged(BR.adsHomeAdapter)
    show.set(true)
  }

  fun follow() {
    followStoreRequest.storeId = storeAdapter.differ.currentList[storeAdapter.position].id
    storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
    storeAdapter.changeFollowingText()
    followingsStoresAdapter.checkFollowingStore()
  }

  fun followingsStores() {
    followStoreRequest.storeId = followingsStoresAdapter.differ.currentList[followingsStoresAdapter.position].id
    storeUseCase.follow(followStoreRequest).launchIn(viewModelScope)
    followingsStoresAdapter.changeFollowingText()
    storeAdapter.checkFollowingStore()
  }



  fun notifyAdapters() {
    notifyPropertyChanged(BR.adsHomeAdapter)
    notifyPropertyChanged(BR.storiesAdapter)
  }

}