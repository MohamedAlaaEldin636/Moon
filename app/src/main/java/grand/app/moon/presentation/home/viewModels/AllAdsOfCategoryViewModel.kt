package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.utils.RVSliderItemImageViewRect15
import grand.app.moon.presentation.home.utils.getAdapterAds
import grand.app.moon.presentation.home.utils.getAdapterSubCategories
import grand.app.moon.presentation.map.MapOfDataFragment
import javax.inject.Inject

@HiltViewModel
class AllAdsOfCategoryViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
	val args: AllAdsOfCategoryFragmentArgs,
) : AndroidViewModel(application) {

	val initialFilter = FilterAllFragment.Filter.fromSpecialString(args.initialSpecialStringFiltering)

	val filter = MutableLiveData(initialFilter)

	val searchQuery = MutableLiveData(initialFilter.search.orEmpty())

	val layoutIsTwoColNotOneCol = MutableLiveData(false)

	val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	val allSubCategories = allCategories.firstOrNull { it.id == args.categoryId }?.subCategories.orEmpty()

	val adapterSubCategories = getAdapterSubCategories(allSubCategories)

	val adapterAds = getAdapterAds()

	val numOfAds = MutableLiveData(0)

	val textOfNumOfAds = numOfAds.map {
		app.getString(R.string.num_of_ads_var_ad, it.orZero().toString())
	}

	val adapterSlider = RVSliderItemImageViewRect15(userLocalUseCase)

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
		val fragment = view.findFragmentOrNull<AllAdsOfCategoryFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<FilterAllFragment.Filter>(
			FilterAllFragment::class.java.name
		) {
			this.filter.value = this.filter.value?.copy(
				search = it.search,
				categoryId = it.categoryId,
				subCategoryId = it.subCategoryId,
				brandId = it.brandId,
				minPrice = it.minPrice,
				maxPrice = it.maxPrice,
				cityId = it.cityId,
				areasIds = it.areasIds,
				properties = it.properties,
				sortBy = it.sortBy,
				adType = it.adType,
				rating = it.rating,
			)

			searchQuery.postValue(
				it.search.orEmpty()
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
		val fragment = view.findFragmentOrNull<AllAdsOfCategoryFragment>() ?: return

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

}
