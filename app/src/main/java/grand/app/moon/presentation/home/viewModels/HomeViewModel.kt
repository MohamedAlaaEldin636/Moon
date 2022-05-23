package grand.app.moon.presentation.home.viewModels

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.Bindable
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.NavHomeDirections
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsHomeAdapter
import grand.app.moon.presentation.base.extensions.navigateSafe
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.home.HomeFragment
import grand.app.moon.presentation.home.HomeFragmentDirections
import grand.app.moon.presentation.story.adapter.StoriesAdapter
import grand.app.moon.presentation.store.adapter.StoreAdapter
import grand.app.moon.presentation.story.adapter.StoriesAllAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

  var storeUseCase: StoreUseCase,
  var userLocalUseCase: UserLocalUseCase,
  val accountRepository: AccountRepository,
  private val homeUseCase: HomeUseCase
) : BaseViewModel() {

  fun checkDeepLink(intent: Intent,v: View) {
    Log.d(TAG, "checkDeepLink: ")
    val action = intent.action
    val data = intent.data
    try {
      if (action != null && data != null) {
        Log.d(TAG, "checkDeepLink: ACTIOM")
        val link = data.toString()
        val parameters = link.split("/").toTypedArray()
        Log.d(TAG, "initDynamicLinkSetDefaultCountry: ${parameters[4]}")
        val id = parameters[5]
        when(parameters[4]){
          "advertisement" -> {
            Log.d(TAG, "initDynamicLinkSetDefaultCountry: done")
            v.findNavController().navigate(
              R.id.nav_ads, bundleOf(
                "id" to id.toInt(),
                "type" to 2
              )
            )
          }
          "shop" -> {
            v.findNavController().navigate(
              R.id.nav_store,
              bundleOf(
                "id" to id.toInt(),
                "type" to 3
              ),Constants.NAVIGATION_OPTIONS)
          }
        }
      }
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

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
  val storiesAllAdapter = StoriesAllAdapter()

  @Bindable
  val categoriesAdapter = CategoriesAdapter()

  @Bindable
  val storeAdapter = StoreAdapter()

  @Inject
  @Bindable
  lateinit var  adsHomeAdapter : AdsHomeAdapter

  var isLoggin = userLocalUseCase.isLoggin()

  fun initAllServices() {
    storeAdapter.percentage = 48
    storeAdapter.isLogin = isLoggin
    storeAdapter.useCase = storeUseCase
    categoriesAdapter.percentage = 33

    getCategories()
    callService()
  }

  fun callService(){
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
    toFilter(v)
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

    val homeList = ArrayList<CategoryAdvertisement>()
    data.categoryAds.forEach {
     if(it.advertisements.size > 0) homeList.add(it)
    }
    homeList.forEach {
      ListHelper.addAllAds(it.advertisements)
    }
    adsHomeAdapter.differ.submitList(homeList)
    notifyPropertyChanged(BR.adsHomeAdapter)
    show.set(true)
  }

}