package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.utils.getAdapterAds
import grand.app.moon.presentation.home.utils.getAdapterCategories
import grand.app.moon.presentation.map.MapOfDataFragment
import javax.inject.Inject

@HiltViewModel
class AllAdsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
	val args: AllAdsFragmentArgs,
) : AndroidViewModel(application) {

	val initialFilter = FilterAllFragment.Filter.fromSpecialString(args.initialSpecialStringFiltering)

	val filter = MutableLiveData(initialFilter)

	val searchQuery = MutableLiveData(initialFilter.search.orEmpty())

	val layoutIsTwoColNotOneCol = MutableLiveData(true)

	val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	val adapterCategories = getAdapterCategories()

	val adapterAds = getAdapterAds()

	fun getOnEditorListener(): TextView.OnEditorActionListener = TextView.OnEditorActionListener { view, actionId, _ ->
		view.findFragmentOrNull<AllAdsFragment>()?.apply {
			context?.apply {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					filter.value = filter.value?.copy(
						search = view?.text.toStringOrEmpty()
					)
				}
			}
		}

		false
	}

	fun changeLayout() {
		layoutIsTwoColNotOneCol.toggleValue()
	}

	fun filter(view: View) {
		val fragment = view.findFragmentOrNull<AllAdsFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<String>(
			FilterAllFragment::class.java.name
		) { specialFilterString ->
			val filter = FilterAllFragment.Filter.fromSpecialString(specialFilterString)


			this.filter.value = this.filter.value?.copy(
				search = filter.search,
				categoryId = filter.categoryId,
				subCategoryId = filter.subCategoryId,
				brandId = filter.brandId,
				minPrice = filter.minPrice,
				maxPrice = filter.maxPrice,
				cityId = filter.cityId,
				areasIds = filter.areasIds,
				properties = filter.properties,
				sortBy = filter.sortBy,
				adType = filter.adType,
				rating = filter.rating,
			)

			searchQuery.postValue(
				filter.search.orEmpty()
			)
		}

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.filter.all",
			paths = arrayOf(
				true.toString(),
				filter.value?.toSpecialString().orStringNullIfNullOrEmpty(),
				true.toString()
			)
		)
	}
	fun sort(view: View) {
		val fragment = view.findFragmentOrNull<AllAdsFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<FilterAllFragment.SortBy>(
			AdsSortDialogFragment::class.java.name
		) {
			filter.value = filter.value?.copy(
				sortBy = it
			)
		}

		AdsSortDialogFragment.launch(
			view.findNavController(),
			args.title,
			filter.value?.sortBy
		)
	}
	fun goToMap(view: View) {
		MapOfDataFragment.goToThisScreenForAds(view.findNavController())
	}

	fun showAllCategories(view: View) {
		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.all.categories",
		)
	}

}
