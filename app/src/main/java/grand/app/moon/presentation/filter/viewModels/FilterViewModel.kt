package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.filter.FilterFragmentDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
  private val useCase: AdsUseCase
) : FilterBaseViewModel(accountRepository) {

  private val TAG = "FilterViewModel"
  val _responseService =
    MutableStateFlow<Resource<BaseResponse<FilterDetails>>>(Resource.Default)
  val response = _responseService

  fun callService() {
    job = useCase.filterDetails(request.categoryId!!, request.sub_category_id!!)
      .onEach {
        response.value = it
      }
      .launchIn(viewModelScope)
  }

  fun setData(response: FilterDetails) {
    viewModelScope.launch {
      accountRepository.getCountries().collect {
        addCities {
          response.filterProperties.add(0, it) //add Citites
        }
        addAnotherOptions{
          response.filterProperties.add(it)
        }
        adapter.differ.submitList(response.filterProperties)
      }
    }
  }

  fun filterSubmit(v: View) {
    prepareRequest()
    v.findNavController()
      .navigate(FilterFragmentDirections.actionFilterFragmentToFilterResultsFragment(request))
  }

}