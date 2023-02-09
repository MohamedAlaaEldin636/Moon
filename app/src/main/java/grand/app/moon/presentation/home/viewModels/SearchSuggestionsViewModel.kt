package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.*
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.databinding.ItemSearchSuggestionsBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.SearchSuggestionsFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSuggestionsViewModel @Inject constructor(
	application: Application,
	val appPreferences: AppPreferences
) : AndroidViewModel(application) {

	val allSuggestions = MutableLiveData(emptyList<String>())

	val searchQuery = MutableLiveData("")

	val availableSuggestions = switchMapMultiple2(allSuggestions, searchQuery) {
		val query = searchQuery.value.orEmpty()
		allSuggestions.value.orEmpty().filter { it.contains(query) }
	}
	val showEmptyView = availableSuggestions.map {
		it.isEmpty()
	}

	val adapter = RVItemCommonListUsage<ItemSearchSuggestionsBinding, String>(
		R.layout.item_search_suggestions,
		onItemClick = { _, binding ->
			binding.root.findFragmentOrNull<SearchSuggestionsFragment>()?.goToSearchSuggestionsFragment(
				binding.textView.text.toStringOrEmpty()
			)
		}
	) { binding, _, item ->
		binding.textView.text = item
	}

	fun getOnEditorListener(): TextView.OnEditorActionListener = TextView.OnEditorActionListener { view, actionId, event ->
		view.findFragmentOrNull<SearchSuggestionsFragment>()?.apply {
			context?.apply {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					goToSearchSuggestionsFragment(
						view?.text.toStringOrEmpty()
					)
				}
			}
		}

		false
	}

	private fun SearchSuggestionsFragment.goToSearchSuggestionsFragment(text: String) {
		if (text.isEmpty()) {
			context?.showError(getString(R.string.at_least_1_type_char))
		}else {
			viewModelScope.launch {
				appPreferences.setSearchSuggestions(
					(allSuggestions.value.orEmpty() + listOf(text)).distinct()
				)

				binding.root.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.search.results",
					paths = arrayOf(text)
				)
			}
		}
	}

}
