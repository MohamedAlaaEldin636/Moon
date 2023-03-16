package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentCategoryDetails2Binding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.viewModels.CategoryDetails2ViewModel

@AndroidEntryPoint
class CategoryDetails2Fragment : BaseFragment<FragmentCategoryDetails2Binding>() {

	companion object {
		fun launch(navController: NavController, categoryId: Int, categoryName: String) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.category.details.two",
				paths = arrayOf(categoryId.toString(), categoryName)
			)
		}
	}

	private val viewModel by viewModels<CategoryDetails2ViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_category_details_2

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getCategoryStories(viewModel.args.categoryId)
			},
			hideLoadingCode = {}
		) { response ->
			val souqMoonStory: ResponseStory? = response.souqMoonStory?.let {
				if (it.stories.isNullOrEmpty()) null else it.copy(isSouqMoonStory = true)
			}

			val allStories = listOfNotNull(souqMoonStory) + response.stories.orEmpty().sortedBy {
				if (it.isSeen) 1 else 0
			}

			viewModel.adapterStories.submitList(listOf(ResponseStory()) + allStories)

			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.repoShop.getCategoryDetails(viewModel.args.categoryId)
				},
				showLoadingCode = {}
			) { categoryDetails ->
				viewModel.adapterStores.submitList(categoryDetails.stores.orEmpty())
				viewModel.showStores.value = categoryDetails.stores.isNullOrEmpty().not()

				viewModel.adapterAds.submitList(categoryDetails.advertisements.orEmpty())
				viewModel.showAds.value = categoryDetails.advertisements.isNullOrEmpty().not()

				viewModel.changeAdsCount(categoryDetails.adsCount.orZero())
			}
		}

		binding.recyclerViewStories.setupWithRVItemCommonListUsage(
			viewModel.adapterStories,
			true,
			1
		) { layoutParams ->
			val number = 4
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth - itemMargins) / number
		}

		binding.recyclerViewStores.setupWithRVItemCommonListUsage(
			viewModel.adapterStores,
			true,
			1
		) { layoutParams ->
			val number = 2
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth - itemMargins) / number
		}

		binding.recyclerViewBrands.setupWithRVItemCommonListUsage(
			viewModel.adapterBrands,
			true,
			2
		) { layoutParams ->
			val number = 3
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth - itemMargins) / number
			layoutParams.height = viewModel.dpToPx91
		}

		binding.recyclerViewSubCategories.setupWithRVItemCommonListUsage(
			viewModel.adapterSubCategories,
			true,
			2
		) { layoutParams ->
			val number = 3
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth - itemMargins) / number
			layoutParams.height = viewModel.dpToPx91
		}

		binding.recyclerViewAds.setupWithRVItemCommonListUsage(
			viewModel.adapterAds,
			true,
			1
		) { layoutParams ->
			val number = 1
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth - itemMargins) / number
		}
	}

}
