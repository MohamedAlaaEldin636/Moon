package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentSearchSuggestionsBinding
import grand.app.moon.extensions.MADividerItemDecoration
import grand.app.moon.extensions.addUniqueTypeItemDecoration
import grand.app.moon.extensions.setOnEditorActionListenerBA
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.SearchSuggestionsViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class SearchSuggestionsFragment : BaseFragment<FragmentSearchSuggestionsBinding>() {

	private val viewModel by viewModels<SearchSuggestionsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_search_suggestions

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			true,
			1
		)
		binding.recyclerView.addUniqueTypeItemDecoration(
			MADividerItemDecoration(
				horizontalSpacing = requireContext().dpToPx(0.7f).roundToInt(),
				dividerColor = ContextCompat.getColor(requireContext(), R.color.search_screen_divider)
			)
		)

		viewModel.availableSuggestions.observe(viewLifecycleOwner) {
			viewModel.adapter.submitList(it.orEmpty())
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.allSuggestions.value = viewModel.appPreferences.getSearchSuggestions()
			}
		}

		binding.editText.setOnEditorActionListenerBA(viewModel.getOnEditorListener())
	}

}
