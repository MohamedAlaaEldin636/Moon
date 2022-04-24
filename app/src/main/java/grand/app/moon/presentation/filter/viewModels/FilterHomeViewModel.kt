package grand.app.moon.presentation.filter.viewModels

import android.view.View
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.presentation.filter.FilterFragmentDirections
import grand.app.moon.presentation.filter.FilterHomeFragmentDirections
import javax.inject.Inject

@HiltViewModel
class FilterHomeViewModel @Inject constructor(
  private val accountRepository: AccountRepository,
) : FilterBaseViewModel(accountRepository) {

  val list = ArrayList<FilterProperty>()
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
    adapter.differ.submitList(list)
  }

  fun filterSubmit(v: View) {
    prepareRequest()
    v.findNavController()
      .navigate(FilterHomeFragmentDirections.actionFilterHomeFragmentToFilterResultsFragment(request))
  }
}