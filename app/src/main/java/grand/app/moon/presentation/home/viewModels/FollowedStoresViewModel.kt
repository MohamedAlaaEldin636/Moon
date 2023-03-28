package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemFollowedStoreBinding
import grand.app.moon.databinding.ItemHomeRvCategoryBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.FollowedStoresFragment
import grand.app.moon.presentation.home.models.ResponseStoreDetails
import grand.app.moon.presentation.home.models.toItemStoreInResponseHome
import javax.inject.Inject

@HiltViewModel
class FollowedStoresViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val showWholePageLoader = MutableLiveData(true)

	private val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	val selectedCategory = MutableLiveData<ItemCategory?>(null)

	val adapterCategories = RVItemCommonListUsage<ItemHomeRvCategoryBinding, ItemCategory>(
		R.layout.item_home_rv_category,
		allCategories,
		onItemClick = { adapter, binding ->
			val item = binding.root.getTagJson<ItemCategory>()
			val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@RVItemCommonListUsage

			val previousSelectionPosition = allCategories.indexOfFirstOrNull { it.id == selectedCategory.value?.id }

			selectedCategory.value = if (position == previousSelectionPosition) null else allCategories.firstOrNull {
				it.id == item?.id
			}

			adapter.notifyItemsChanged(previousSelectionPosition, position)
		}
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.root.setTagJson(item)
		binding.root.setTag(R.id.position_tag, position)

		binding.textTextView.text = item.name

		binding.imageImageView.setupWithGlide {
			load(item.image)
		}

		val isSelected = selectedCategory.value?.id == item.id
		binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(
			ContextCompat.getColor(context, if (isSelected) R.color.colorPrimary else R.color.white)
		)
		binding.textTextView.setTextColor(
			ContextCompat.getColor(context, if (isSelected) R.color.white else R.color.colorPrimary)
		)
	}

	val adapterStores = RVPagingItemCommonListUsage<ItemFollowedStoreBinding, ResponseStoreDetails>(
		R.layout.item_followed_store,
		onItemClick = { _, binding ->
			val item = binding.root.getTagJson<ResponseStoreDetails>() ?: return@RVPagingItemCommonListUsage

			userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
				binding.root.context ?: return@RVPagingItemCommonListUsage,
				binding.root.findNavController(),
				item
			)
		},
		additionalListenersSetups = { _, binding ->
			binding.buttonConstraintLayout.setOnClickListener {
				val item = binding.root.getTagJson<ResponseStoreDetails>() ?: return@setOnClickListener

				val fragment = binding.root.findFragmentOrNull<FollowedStoresFragment>() ?: return@setOnClickListener

				fragment.handleRetryAbleActionCancellableNullable(
					action = {
						// Unfollow
						repoShop.followStore(item.id.orZero())
					}
				) {
					fragment.context.followOrUnFollowStoreFromNotHomeScreen(item.copy(isFollowing = false).toItemStoreInResponseHome())

					fragment.reFetchData()
				}
			}
		}
	) { binding, _, item ->
		binding.root.setTagJson(item)

		binding.imageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
		}

		binding.nameTextView.text = item.name.orEmpty()

		binding.ratingBar.setProgressBAFloat(item.averageRate.orZero()/* * 20f*/)
		binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
			.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

		binding.viewsTextView.text = item.viewsCount.toStringOrEmpty()

		binding.adsTextView.text = "${item.advertisementsCount.orZero()} ${app.getString(R.string.advertisement)}"
	}

	fun showAllCategories(view: View) {
		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.all.categories",
		)
	}

}
