package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.data.home2.RepoHome2
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.map.MapOfDataFragment
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CategoryDetails2ViewModel @Inject constructor(
	application: Application,
	val args: CategoryDetails2FragmentArgs,
	val userLocalUseCase: UserLocalUseCase,
	val repoHome2: RepoHome2,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val dpToPx91 by lazy {
		app.dpToPx(91f).roundToInt()
	}

	val storesLabelText by lazy {
		"${app.getString(R.string.stores)} ${args.categoryName}"
	}

	private val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	private val allSubCategories = allCategories.firstOrNull {
		it.id == args.categoryId
	}?.subCategories.orEmpty()

	private val allBrands = allCategories.firstOrNull {
		it.id == args.categoryId
	}?.brands.orEmpty()

	val showStores = MutableLiveData(true)
	val showAds = MutableLiveData(true)
	val showBrands = allBrands.isNotEmpty()

	val adapterStories = getAdapterStories()

	val adapterStores = getAdapterStores()

	val adapterSubCategories = getSubCategoriesOrBrandsAdapter(allSubCategories, true)

	val adapterBrands = getSubCategoriesOrBrandsAdapter(allBrands, false)

	val adapterAds = getAdapterAds()

	fun goToMap(view: View) {
		MapOfDataFragment.goToThisScreenForStores(view.findNavController())
	}

	fun showAllStores(view: View) {
		AllStoresFragment.launch(
			view.findNavController(),
			AllStoresFragment.Filter(categoryId = args.categoryId),
			app.getString(R.string.stores_879)
		)
	}

	fun showAllBrandsAndAllSubCategories(view: View) {
		AllAdsOfCategoryFragment.launch(
			view.findNavController(),
			FilterAllFragment.Filter(
				categoryId = args.categoryId,
			),
			args.categoryName,
			args.categoryId
		)
	}

	fun showAllSpecialAds(view: View) {
		AllAdsOfCategoryFragment.launch(
			view.findNavController(),
			FilterAllFragment.Filter(
				categoryId = args.categoryId,
				adType = FilterAllFragment.AdType.PREMIUM
			),
			args.categoryName,
			args.categoryId
		)
	}

	fun search(view: View) {
		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.search.suggestions",
		)
	}

}
