package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.filter.FilterHomeFragmentDirections
import grand.app.moon.presentation.filter.adapter.FilterRateAdapter
import javax.inject.Inject

@HiltViewModel
class FilterHomeViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
) : FilterBaseViewModel(accountRepository) {

  val filterAdapter = FilterRateAdapter()

  private val TAG = "FilterHomeViewModel"

  val list = ArrayList<FilterProperty>()
  val rates = ArrayList<FilterProperty>()
  init {
    addCategories{
      list.add(it)
    }
    addSubCategories{
      list.add(it)
    }
    addCities{
      list.add(it)
    }
    addPrice{
      list.add(it)
    }
    addSortBy{
      list.add(it)
    }
    addAnotherOptions{
      list.add(it)
    }
    addRates{
      rates.add(it)
    }
    Log.d(TAG, ": ${rates.size}")
    adapter.differ.submitList(list)
    filterAdapter.differ.submitList(rates[0].children)
  }


  fun filterSubmit(v: View) {
    if(filterAdapter.lastSelected != -1){
      request.min_rate = filterAdapter.differ.currentList[filterAdapter.lastSelected].id
    }
    Log.d(TAG, "filterSubmit: ${request.min_rate}")
    prepareRequest()
    v.findNavController()
      .navigate(FilterHomeFragmentDirections.actionFilterHomeFragmentToFilterResultsFragment(request))
  }
}