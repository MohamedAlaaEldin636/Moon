package grand.app.moon.presentation.home.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import grand.app.moon.presentation.store.adapter.StoreisAdapter
import grand.app.moon.presentation.story.adapter.StoreAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  val accountRepository: AccountRepository,
  private val homeUseCase: HomeUseCase) : BaseViewModel() {

  private val _homeResponse =
    MutableStateFlow<Resource<BaseResponse<HomeResponse>>>(Resource.Default)
  val homeResponse = _homeResponse

  private val _storiesResponse =
    MutableStateFlow<Resource<BaseResponse<ArrayList<StoryItem>>>>(Resource.Default)
  val storiesResponse = _storiesResponse


  var titleToolbar = MutableLiveData<String>("")

  @Bindable
  val storiesAdapter = StoreisAdapter()

  @Bindable
  val categoriesAdapter = CategoriesAdapter()

  @Bindable
  val storeAdapter = StoreAdapter()

  init {
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

  private fun homeApi() {
    homeUseCase.invoke()
      .onEach { result ->
        _homeResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  private fun getStories(){
    homeUseCase.getStories()
      .onEach { result ->
        storiesResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun updateStories(data: MutableList<StoryItem>) {
    storiesAdapter.differ.submitList(data)
    notifyPropertyChanged(BR.storiesAdapter)
  }

  fun updateList(data: HomeResponse) {
    storeAdapter.differ.submitList(data.mostRatedStores)
    notifyPropertyChanged(BR.storeAdapter)
  }
}