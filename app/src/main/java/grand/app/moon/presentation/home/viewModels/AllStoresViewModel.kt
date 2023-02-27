package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.content.res.ColorStateList
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvCategoryBinding
import grand.app.moon.databinding.ItemHomeRvStoreBinding
import grand.app.moon.databinding.ItemHomeRvStoreSingleColBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.extensions.bindingAdapter.setCompoundDrawablesRelativeWithIntrinsicBoundsStart
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome
import grand.app.moon.presentation.map.MapOfDataFragment
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class AllStoresViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
	val args: AllStoresFragmentArgs,
) : AndroidViewModel(application) {

	private val initialFilter = args.jsonOfFilter.fromJsonInlinedOrNull<AllStoresFragment.Filter>()
		?: AllStoresFragment.Filter()

	val filter = MutableLiveData(initialFilter)

	val searchQuery = MutableLiveData(initialFilter.search.orEmpty())

	val layoutIsTwoColNotOneCol = MutableLiveData(true)

	private val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	val adapterCategories = RVItemCommonListUsage<ItemHomeRvCategoryBinding, ItemCategory>(
		R.layout.item_home_rv_category,
		allCategories,
		onItemClick = { adapter, binding ->
			val item = binding.root.getTagJson<ItemCategory>()
			val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@RVItemCommonListUsage

			val previousSelectionPosition = allCategories.indexOfFirstOrNull { it.id == filter.value?.categoryId }

			filter.value = filter.value?.copy(
				categoryId = if (position == previousSelectionPosition) null else item?.id
			)

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

		val isSelected = filter.value?.categoryId == item.id
		binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(
			ContextCompat.getColor(context, if (isSelected) R.color.colorPrimary else R.color.white)
		)
		binding.textTextView.setTextColor(
			ContextCompat.getColor(context, if (isSelected) R.color.white else R.color.colorPrimary)
		)
	}

	val adapterStores = RVPagingItemCommonListUsageWithDifferentItems<ItemStoreInResponseHome>(
		getLayoutRes = {
			if (layoutIsTwoColNotOneCol.value == true) {
				R.layout.item_home_rv_store
			}else {
				R.layout.item_home_rv_store_single_col
			}
		},
		onItemClick = { _, binding ->
			val context = binding.root.context ?: return@RVPagingItemCommonListUsageWithDifferentItems

			val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@RVPagingItemCommonListUsageWithDifferentItems

			userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
				context,
				binding.root.findNavController(),
				item,
			)
		},
		additionalListenersSetups = { adapter, binding ->
			when (binding) {
				is ItemHomeRvStoreBinding -> {
					binding.followingButtonView.setOnClickListener { view ->
						val context = view.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@setOnClickListener
						val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							context.applicationScope?.launch {
								repoShop.followStore(item.id.orZero())
							}

							adapter.updateItem(
								position
							) {
								it.isFollowing = it.isFollowing.orFalse().not()
							}
						}
					}
				}
				is ItemHomeRvStoreSingleColBinding -> {
					binding.followingButtonTextView.setOnClickListener { view ->
						val context = view.context ?: return@setOnClickListener

						val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@setOnClickListener
						val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

						if (context.isLoginWithOpenAuth()) {
							context.applicationScope?.launch {
								repoShop.followStore(item.id.orZero())
							}

							adapter.updateItem(
								position
							) {
								it.isFollowing = it.isFollowing.orFalse().not()
							}
						}
					}
				}
			}
		}
	) { binding, position, item ->
		binding.root.tag = item.toJsonInlinedOrNull()
		binding.root.setTag(R.id.position_tag, position)

		val context = binding.root.context ?: return@RVPagingItemCommonListUsageWithDifferentItems

		when (binding) {
			is ItemHomeRvStoreBinding -> {
				binding.imageImageView.setupWithGlide {
					load(item.image).placeholder(R.drawable.splash)
				}

				binding.nameTextView.text = item.name
				binding.nameTextView.setCompoundDrawablesRelativeWithIntrinsicBoundsStart(
					if (item.hasOffer.orFalse()) R.drawable.store_has_offer else 0
				)

				binding.nicknameTextView.text = item.nickname

				binding.ratingBar.setProgressBA((item.averageRate.orZero() * 20).roundToInt())

				binding.averageRateTextView.text = "( ${item.averageRate?.round(1).orZero()} )"

				binding.viewsTextView.text = item.viewsCount.toStringOrEmpty()

				binding.adsTextView.text = "${item.advertisementsCount.orZero()} ${context.getString(R.string.advertisement)}"

				binding.followingButtonTextView.text = if (item.isFollowing.orFalse()) {
					binding.followingButtonTextView.serDrawableCompatBA()

					context.getString(R.string.un_follow)
				}else {
					binding.followingButtonTextView.serDrawableCompatBA(
						start = ContextCompat.getDrawable(context, R.drawable.follow_add)
					)

					context.getString(R.string.follow)
				}

				binding.premiumImageView.isVisible = item.isPremium
			}
			is ItemHomeRvStoreSingleColBinding -> {
				binding.imageImageView.setupWithGlide {
					load(item.backgroundImage).placeholder(R.drawable.splash)
				}

				binding.logoImageView.setupWithGlide {
					load(item.image).placeholder(R.drawable.splash)
				}

				binding.nameTextView.text = item.name
				binding.nameTextView.setCompoundDrawablesRelativeWithIntrinsicBoundsStart(
					if (item.hasOffer.orFalse()) R.drawable.store_has_offer else 0
				)

				binding.nicknameTextView.text = item.nickname

				binding.ratingBar.setProgressBA((item.averageRate.orZero() * 20).roundToInt())

				binding.averageRateTextView.text = "( ${item.averageRate?.round(1).orZero()} )"

				binding.viewsTextView.text = item.viewsCount.toStringOrEmpty()

				binding.adsTextView.text = "${item.advertisementsCount.orZero()} ${context.getString(R.string.advertisement)}"

				binding.followingButtonTextView.text = if (item.isFollowing.orFalse()) {
					binding.followingButtonTextView.serDrawableCompatBA()

					context.getString(R.string.un_follow)
				}else {
					binding.followingButtonTextView.serDrawableCompatBA(
						start = ContextCompat.getDrawable(context, R.drawable.follow_add)
					)

					context.getString(R.string.follow)
				}

				binding.premiumImageView.isVisible = item.isPremium
			}
		}
	}

	fun getOnEditorListener(): TextView.OnEditorActionListener = TextView.OnEditorActionListener { view, actionId, _ ->
		view.findFragmentOrNull<AllStoresFragment>()?.apply {
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
		val fragment = view.findFragmentOrNull<AllStoresFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<AllStoresFragment.Filter>(
			FilterAllFragment::class.java.name
		) {
			filter.value = filter.value?.copy(
				search = it.search,
				categoryId = it.categoryId,
				subCategoryId = it.subCategoryId,
				cityId = it.cityId,
				areasIds = it.areasIds,
				//sortBy = null,
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
				false.toString(),
				filter.value?.toFilterAllFragmentFilter()?.toSpecialString().orStringNullIfNullOrEmpty()
			)
		)
	}
	fun sort(view: View) {
		val fragment = view.findFragmentOrNull<AllStoresFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<AllStoresFragment.SortBy>(
			StoresSortDialogFragment::class.java.name
		) {
			filter.value = filter.value?.copy(
				sortBy = it
			)
		}

		StoresSortDialogFragment.launch(
			view.findNavController(),
			args.title,
			filter.value?.sortBy
		)
	}
	fun goToMap(view: View) {
		MapOfDataFragment.goToThisScreenForStores(view.findNavController())
	}

	fun showAllCategories(view: View) {
		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.all.categories",
		)
	}

}
