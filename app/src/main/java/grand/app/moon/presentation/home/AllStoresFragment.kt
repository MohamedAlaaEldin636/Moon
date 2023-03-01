package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAllStoresBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.viewModels.AllStoresViewModel

@AndroidEntryPoint
class AllStoresFragment : BaseFragment<FragmentAllStoresBinding>() {

	companion object {
		fun launch(navController: NavController, filter: Filter?, title: String) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.all.stores",
				paths = arrayOf(title, filter.toJsonInlinedOrNull().orStringNullIfNullOrEmpty())
			)
		}
	}

	private val viewModel by viewModels<AllStoresViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getAllStores(viewModel.filter.value ?: Filter())
			},
			collectLatestAction = {
				viewModel.adapterStores.submitData(it)
			},
			onRetry = {
				viewModel.adapterStores.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())

				viewModel.adapterStores.refresh()
			}
		)
	}

	override fun getLayoutId(): Int = R.layout.fragment_all_stores

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		retryAbleFlow.collectLatest()

		binding.recyclerViewCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterCategories,
			true,
			1
		) { layoutParams ->
			val number = 4
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth + layoutParams.marginEnd) / number
		}

		binding.recyclerViewStores.setupWithRVItemCommonListUsage(
			viewModel.adapterStores.withDefaultHeaderAndFooterAdapters(),
			false,
			2,
			onGridLayoutSpanSizeLookup = {
				if (viewModel.layoutIsTwoColNotOneCol.value == true) 1 else 2
			}
		)

		viewModel.layoutIsTwoColNotOneCol.distinctUntilChanged().ignoreFirstTimeChanged().observe(viewLifecycleOwner) {
			binding.recyclerViewStores.layoutManager?.requestLayout()
		}

		binding.searchEditText.setOnEditorActionListenerBA(viewModel.getOnEditorListener())

		viewModel.filter.observe(viewLifecycleOwner) {
			retryAbleFlow.retry()
		}

		binding.rootConstraintLayout.post {
			val layoutParams = binding.recyclerViewStores.layoutParams
			layoutParams.height = binding.rootConstraintLayout.height
			binding.recyclerViewStores.layoutParams = layoutParams
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<String>(QRCodeScannerFragment::class.java.name) {
			// Example -> https://eg.sooqmoon.net/ar/shop/9852/m1006mmm

			val endIndex = it.lastIndexOf("/")
			val id = if (endIndex.dec() < 0) null else {
				val index = it.lastIndexOf("/", startIndex = endIndex.dec())
				if (index.inc() < 0) null else it.substring(index.inc(), endIndex).toIntOrNull()
			}

			if (id == null) {
				showMessage(getString(R.string.something_went_wrong_please_try_again))

				return@observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
			}

			context?.also { context ->
				viewModel.userLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
					context,
					findNavController(),
					id
				)
			}
		}
	}

	data class Filter(
		val search: String? = null,
		val categoryId: Int? = null,
		val subCategoryId: Int? = null,
		val cityId: Int? = null,
		val areasIds: List<Int>? = null,
		val sortBy: SortBy? = null,
		val rating: Int? = null,

		//val minPrice: Float? = null,
		//val maxPrice: Float? = null,
		//val properties: List<DynamicFilterProperty> = emptyList(),
		//val adType: FilterAllFragment.AdType? = null,
	) {
		fun toFilterAllFragmentFilter(): FilterAllFragment.Filter {
			return FilterAllFragment.Filter(
				search,
				categoryId,
				subCategoryId,
				null,
				null,
				null,
				cityId,
				areasIds,
				emptyList(),
				null, // Won't be used in case of store
				null,
				rating
			)
		}
	}

	enum class SortBy(val apiValue: Int) {
		NEWEST(1), OLDEST(2), HIGHEST_RATED(3)
	}

}
