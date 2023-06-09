package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.postDelayed
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

	val showWholePageLoader = MutableLiveData(true)

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

	val showSlider = MutableLiveData(true)

	fun getOnEditorListener(): TextView.OnEditorActionListener = TextView.OnEditorActionListener { view, actionId, _ ->
		view.findFragmentOrNull<AllAdsOfCategoryFragment>()?.apply {
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

		fragment.setFragmentResultListenerUsingJson<String>(
			FilterAllFragment::class.java.name
		) {specialFilterString ->
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
		val fragment = view.findFragmentOrNull<AllAdsOfCategoryFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<FilterAllFragment.SortBy>(
			AdsSortDialogFragment::class.java.name
		) {
			filter.value = filter.value?.copy(
				sortBy = it
			)
		}

		val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
			val toolbarName = allCategories.firstOrNull {
				it.id == filter.value?.categoryId
			}?.name.letIfNullOrEmpty { args.title }
			(fragment.activity as? HomeActivity)?.binding?.toolbar?.title = toolbarName
		}

		view.findNavController().addOnDestinationChangedListener(listener)

		fragment.setFragmentResultListenerUsingJson<Boolean>(
			AdsSortDialogFragment.FRAGMENT_RESULT_ON_EITHER_CANCEL_OR_DISMISS
		) { bool ->
			if (bool) {
				MyLogger.e("diuihsidauhsuis")

				view.findNavController().removeOnDestinationChangedListener(listener)

				val toolbarName = allCategories.firstOrNull {
					it.id == filter.value?.categoryId
				}?.name.letIfNullOrEmpty { args.title }
				(fragment.activity as? HomeActivity)?.binding?.toolbar?.title = toolbarName
				(fragment.activity as? HomeActivity)?.binding?.toolbar?.postDelayed(50) {
					(fragment.activity as? HomeActivity)?.binding?.toolbar?.title = toolbarName
				}
			}
		}

		val toolbarName = allCategories.firstOrNull {
			it.id == filter.value?.categoryId
		}?.name.letIfNullOrEmpty { args.title }
		AdsSortDialogFragment.launch(
			view.findNavController(),
			toolbarName,
			filter.value?.sortBy
		)
	}
	fun goToMap(view: View) {
		MapOfDataFragment.goToThisScreenForAds(view.findNavController())
	}

}
