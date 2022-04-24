package grand.app.moon.presentation.subCategory.viewModel

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.BuildConfig
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.adapter.StoriesAdapter
import grand.app.moon.presentation.subCategory.SubCategoryFragmentDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
  private val useCase: AdsUseCase,
  private val accountRepository: AccountRepository,

  ) : BaseViewModel() {
  @Bindable
  var page: Int = 0

  @Bindable
  var callingService = false

  var subCategoryId: Int = -1
  var categoryId: Int = -1

  var isLast = false


  val subCategoryResponse = ObservableField<SubCategoryResponse>()
  val gridOne = ObservableBoolean(true)

  var sortBy = 1

  @Bindable
  var adapter = AdsAdapter()

  var ADS_LIST_URL = ""

  val _responseService =
    MutableStateFlow<Resource<BaseResponse<SubCategoryResponse>>>(Resource.Default)

  val response = _responseService

  init {
    adapter.percentageAds = 100
  }

  fun callService() {
    if (!callingService && !isLast) {
      callingService = true
      notifyPropertyChanged(BR.callingService)
      page++
      if (page > 1) {
        notifyPropertyChanged(BR.page)
      }
      getAdsList()
    }
  }

  fun reset() {
    page = 0
    callingService = false
    isLast = false
  }

  private fun getAdsList() {
    ADS_LIST_URL =
      "${BuildConfig.API_BASE_URL}v1/advertisements?page=${page}&sub_category_id=${subCategoryId}&order_by=$sortBy"
    job = useCase.getAdsSubCategory(ADS_LIST_URL)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  fun filter(v: View) {
    v.findNavController().navigate(
      SubCategoryFragmentDirections.actionSubCategoryFragmentToFilterFragment(
        categoryId,
        subCategoryId
      )
    )
  }

  fun filterSort(v: View) {
    v.findNavController().navigate(
      SubCategoryFragmentDirections.actionSubCategoryFragmentToFilterSortDialog(
        sortBy,
        FilterDialog.ADVERTISEMENT
      )
    )
  }

  fun map(v: View) {
    this.subCategoryResponse.get()?.let {
      val action =
        SubCategoryFragmentDirections.actionSubCategoryFragmentToMapSubCategoryFragment(it)
      action.subCategory = subCategoryId
      v.findNavController().navigate(action)
    }
  }

  private val TAG = "PackagesViewModel"

  fun setData(data: SubCategoryResponse) {
    this.subCategoryResponse.set(data)
    data.let {
      println("size:" + data.advertisements.list.size)
      isLast = data.advertisements.links.next == null
      if (page == 1) {
//        adapter = InvoicesAdapter()
        adapter.differ.submitList(it.advertisements.list)
        show.set(true)
        notifyPropertyChanged(BR.adapter)
      } else {
        adapter.insertData(it.advertisements.list)
      }
      callingService = false
      notifyPropertyChanged(BR.callingService)
    }
  }


  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun setCategoryId() {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        it.data.forEach { categoryItem ->
          categoryItem.subCategories?.forEach { subCategory ->
            if (subCategory.id == subCategoryId) {
              categoryId = categoryItem.id!!
              return@forEach
            }
          }
        }
      }
    }
  }

  fun changeGrid() {
    if (adapter.grid == Constants.GRID_2) {
      adapter.grid = Constants.GRID_1
      clickEvent.value = Constants.GRID_1
    } else {
      adapter.grid = Constants.GRID_2
      clickEvent.value = Constants.GRID_2
    }
  }
}