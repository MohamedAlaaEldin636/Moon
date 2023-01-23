package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.databinding.ItemStoreFullDataBinding
import grand.app.moon.domain.myStore.ResponseMyStoreDetails
import grand.app.moon.extensions.*
import grand.app.moon.presentation.myStore.model.ItemStoreInfo
import javax.inject.Inject

@HiltViewModel
class StoreFullDataViewModel @Inject constructor(
	application: Application,
	val repoPackages: RepositoryPackages
) : AndroidViewModel(application) {

	val list = listOf(
		ItemStoreInfo(
			R.drawable.ic_store_data_4,
			false,
			R.string.store_data
		),
		ItemStoreInfo(
			R.drawable.ic_stats_4,
			false,
			R.string.clients_stats
		),
		ItemStoreInfo(
			R.drawable.ic_category_4,
			false,
			R.string.your_store_main_categories
		),
		ItemStoreInfo(
			R.drawable.ic_sub_category_4,
			false,
			R.string.your_store_sub_categories
		),
		ItemStoreInfo(
			R.drawable.ic_working_hours_4,
			false,
			R.string.working_hours
		),
		ItemStoreInfo(
			R.drawable.ic_social_4,
			false,
			R.string.your_store_social_media
		),
		ItemStoreInfo(
			R.drawable.ic_reviews_4,
			false,
			R.string.cients_opionions
		),
		ItemStoreInfo(
			R.drawable.ic_packages_4,
			false,
			R.string.packages_898
		),
		ItemStoreInfo(
			R.drawable.ic_store_story_4,
			false,
			R.string.store_story
		),
		ItemStoreInfo(
			R.drawable.ic_store_explore_4,
			false,
			R.string.store_explore
		),
	)

	val response = MutableLiveData<ResponseMyStoreDetails?>()

	val progressFromZeroToHundred = response.map {
		it?.progressBar.orZero()
	}
	val progressFromZeroToOne = progressFromZeroToHundred.map {
		it.toFloat() / 100f
	}
	val progressString = progressFromZeroToHundred.map {
		"$it %"
	}

	val adapter = RVItemCommonListUsage<ItemStoreFullDataBinding, ItemStoreInfo>(
		R.layout.item_store_full_data,
		emptyList(),
		onItemClick = { _, binding ->
			val navController = binding.root.findNavController()

			when (binding.constraintLayout.tag as? Int) {
				R.drawable.ic_store_data_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.create.store"
					)
				}
				R.drawable.ic_stats_4 -> {
					General.TODO("Not programmed yet.")
				}
				R.drawable.ic_category_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.store.categories.a"
					)
				}
				R.drawable.ic_sub_category_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.store.sub.categories.a"
					)
				}
				R.drawable.ic_working_hours_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.working.hours"
					)
				}
				R.drawable.ic_social_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.store.social.media"
					)
				}
				R.drawable.ic_reviews_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.store.clients.reviews"
					)
				}
				R.drawable.ic_packages_4 -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.become.shop.packages"
					)
				}
				R.drawable.ic_store_story_4 -> {
					General.TODO("Not programmed yet.")
				}
				R.drawable.ic_store_explore_4 -> {
					General.TODO("Not programmed yet.")
				}
			}
		}
	) { binding, _, item ->
		binding.constraintLayout.tag = item.imageDrawableRes

		binding.imageImageView.setImageResource(item.imageDrawableRes)

		binding.notCompleteImageView.isVisible = item.notComplete

		binding.textTextView.text = binding.root.context.getString(item.nameStringRes)
	}



}
