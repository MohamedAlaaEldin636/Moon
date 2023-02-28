package grand.app.moon.presentation.home

import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAllAdsOfCategoryBinding
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.orStringNullIfNullOrEmpty
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.AllAdsOfCategoryViewModel

@AndroidEntryPoint
class AllAdsOfCategoryFragment : BaseFragment<FragmentAllAdsOfCategoryBinding>() {

	companion object {
		fun launch(
			navController: NavController,
			filter: FilterAllFragment.Filter?,
			title: String,
			categoryId: Int
		) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.all.ads.of.category",
				paths = arrayOf(
					title,
					filter?.toSpecialString().orStringNullIfNullOrEmpty(),
					categoryId.toString()
				)
			)
		}
	}

	private val viewModel by viewModels<AllAdsOfCategoryViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_all_ads_of_category

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}
