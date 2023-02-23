package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentFilterAllBinding
import grand.app.moon.domain.ads.DynamicFilterProperty
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.extensions.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.FilterAllViewModel
import grand.app.moon.presentation.home.viewModels.FilterNavHomeGraphViewModel

@AndroidEntryPoint
class FilterAllFragment : BaseFragment<FragmentFilterAllBinding>() {

	private val viewModel by viewModels<FilterAllViewModel>()

	val navGraphViewModel by navGraphViewModels<FilterNavHomeGraphViewModel>(R.id.nav_home)

	override fun getLayoutId(): Int = R.layout.fragment_filter_all

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)

		viewModel.responseFilterProperties.observe(viewLifecycleOwner) {
			viewModel.adapter.submitList(it?.properties.orEmpty())
		}

		/*observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<IdAndName>(SingleOrMultiSelectionFragment.KEY_RESULT_SINGLE_SELECTION) {
			viewModel.changeSelectedCity(it.id.orZero())
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<List<IdAndName>>(SingleOrMultiSelectionFragment.KEY_RESULT_MULTI_SELECTION) { list ->
			viewModel.changeSelectedAreas(list.map { it.id.orZero() })
		}*/
	}

	enum class SortBy(val apiValue: Int) {
		NEWEST(1), OLDEST(2), LEAST_PRICE(3), HIGHEST_PRICE(4)
	}

	enum class AdType(val apiValue: Int) {
		ALL(1), PREMIUM(2), FREE(3)
	}

	data class Filter(
		val search: String? = null,
		val categoryId: Int? = null,
		val subCategoryId: Int? = null,
		val minPrice: Float? = null,
		val maxPrice: Float? = null,
		val cityId: Int? = null,
		val areasIds: List<Int>? = null,
		val properties: List<DynamicFilterProperty> = emptyList(),
		val sortBy: SortBy? = null,
		val adType: AdType? = null,
		val rating: Int? = null,
	)

}
