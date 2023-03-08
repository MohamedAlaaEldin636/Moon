package grand.app.moon.presentation.home.utils

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.databinding.ItemHomeRvAdvBinding
import grand.app.moon.databinding.ItemSearchResultBinding
import grand.app.moon.databinding.ItemStoreCategoryInMyAdsBinding
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ResponseSearchResult
import grand.app.moon.presentation.home.viewModels.AllAdsOfCategoryViewModel
import kotlinx.coroutines.launch

fun AllAdsOfCategoryViewModel.getAdapterSubCategories(
	initialData: List<ItemSubCategory>,
) = RVItemCommonListUsage<ItemStoreCategoryInMyAdsBinding, ItemSubCategory>(
	R.layout.item_store_category_in_my_ads,
	initialData,
	onItemClick = { adapter, binding ->
		val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemSubCategory>() ?: return@RVItemCommonListUsage
		val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@RVItemCommonListUsage

		val previousSelectionPosition = allSubCategories.indexOfFirstOrNull { it.id == filter.value?.subCategoryId }

		filter.value = filter.value?.copy(
			subCategoryId = if (position == previousSelectionPosition) null else item.id,
			properties = emptyList()
		)

		adapter.notifyItemsChanged(previousSelectionPosition, position)
	}
) { binding, position, item ->
	val context = binding.root.context ?: return@RVItemCommonListUsage

	binding.root.tag = item.toJsonInlinedOrNull()
	binding.root.setTag(R.id.position_tag, position)

	binding.textView.text = item.name

	val isSelected = filter.value?.subCategoryId == item.id
	binding.textView.backgroundTintList = ColorStateList.valueOf(
		ContextCompat.getColor(context, if (isSelected) R.color.star_enabled else R.color.colorPrimary)
	)
}

fun AllAdsOfCategoryViewModel.getAdapterAds() = RVPagingItemCommonListUsageWithDifferentItems<ItemAdvertisementInResponseHome>(
	getLayoutRes = {
		if (layoutIsTwoColNotOneCol.value == true) {
			R.layout.item_home_rv_adv
		}else {
			R.layout.item_search_result
		}
	},
	onItemClick = { _, binding ->
		val context = binding.root.context ?: return@RVPagingItemCommonListUsageWithDifferentItems

		val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
			?: return@RVPagingItemCommonListUsageWithDifferentItems

		userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
			context,
			binding.root.findNavController(),
			item
		)
	},
	additionalListenersSetups = { adapter, binding ->
		when (binding) {
			is ItemHomeRvAdvBinding -> {
				val listener = View.OnClickListener {
					val context = binding.root.context ?: return@OnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@OnClickListener

					//val position = binding.linearLayout.tag as? Int ?: return@OnClickListener

					userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
						context,
						binding.root.findNavController(),
						item.store
					)
				}
				binding.storeImageImageView.setOnClickListener(listener)
				binding.storeTextView.setOnClickListener(listener)
				binding.favImageView.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					if (context.isLoginWithOpenAuth()) {
						context.applicationScope?.launch {
							repoShop.favOrUnFavAdv(item.id.orZero())
						}

						adapter.updateItem(
							position,
							//item.copy(isFavorite = item.isFavorite.orFalse().not())
						) {
							it.isFavorite = it.isFavorite.orFalse().not()
						}
					}
				}

				binding.whatsAppImageView.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					context.applicationScope?.launch {
						repoShop.interactionForAdWhatsApp(item.id.orZero())
					}

					context.launchWhatsApp(item.phone.orEmpty())
				}
				binding.callImageView.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					context.applicationScope?.launch {
						repoShop.interactionForAdCall(item.id.orZero())
					}

					context.launchDialNumber(item.phone.orEmpty())
				}
				binding.chatImageView.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					if (context.isLoginWithOpenAuth()) {
						context.applicationScope?.launch {
							repoShop.interactionForAdChat(item.id.orZero())
						}

						item.store?.also {
							context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
						}
					}
				}
			}
			is ItemSearchResultBinding -> {
				val listener = View.OnClickListener {
					val context = binding.root.context ?: return@OnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@OnClickListener

					//val position = binding.linearLayout.tag as? Int ?: return@OnClickListener

					userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
						context,
						binding.root.findNavController(),
						item.store
					)
				}
				binding.storeImageImageView.setOnClickListener(listener)
				binding.storeTextView.setOnClickListener(listener)
				binding.favImageView.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					if (context.isLoginWithOpenAuth()) {
						context.applicationScope?.launch {
							repoShop.favOrUnFavAdv(item.id.orZero())
						}

						adapter.updateItem(
							position
						) {
							it.isFavorite = it.isFavorite.orFalse().not()
						}
					}
				}

				binding.whatsAppConstraintLayout.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					context.applicationScope?.launch {
						repoShop.interactionForAdWhatsApp(item.id.orZero())
					}

					context.launchWhatsApp(item.phone.orEmpty())
				}
				binding.phoneConstraintLayout.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					context.applicationScope?.launch {
						repoShop.interactionForAdCall(item.id.orZero())
					}

					context.launchDialNumber(item.phone.orEmpty())
				}
				binding.chatConstraintLayout.setOnClickListener {
					val context = binding.root.context ?: return@setOnClickListener

					val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
						?: return@setOnClickListener

					if (context.isLoginWithOpenAuth()) {
						context.applicationScope?.launch {
							repoShop.interactionForAdChat(item.id.orZero())
						}

						item.store?.also {
							context.launchCometChat(it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
						}
					}
				}
			}
		}
	}
) { binding, position, item ->
	binding.root.tag = item.toJsonInlinedOrNull()
	binding.root.setTag(R.id.position_tag, position)

	//val context = binding.root.context ?: return@RVPagingItemCommonListUsageWithDifferentItems

	when (binding) {
		is ItemHomeRvAdvBinding -> {
			binding.imageImageView.setupWithGlide {
				load(item.image).saveDiskCacheStrategyAll()
			}

			binding.premiumImageView.isVisible = item.isPremium

			binding.favImageView.setImageResource(
				if (item.isFavorite.orFalse()) R.drawable.item_adv_fav_med_cropped else R.drawable.item_adv_fav_cropped
			)

			binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
				.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

			binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

			binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

			binding.nameTextView.text = item.title

			binding.timeTextView.text = item.createdAt.orEmpty()

			binding.placeTextView.text = "${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}"

			binding.storeImageImageView.setupWithGlide {
				load(item.store?.image).saveDiskCacheStrategyAll()
			}

			binding.storeTextView.text = item.store?.name

			binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

			binding.negotiableTextView.isVisible = item.isNegotiable
		}
		is ItemSearchResultBinding -> {
			binding.imageImageView.setupWithGlide {
				load(item.image)
			}

			binding.premiumImageView.isVisible = item.isPremium

			binding.favImageView.setImageResource(
				if (item.isFavorite.orFalse()) R.drawable.item_adv_fav_med_cropped else R.drawable.item_adv_fav_large_cropped
			)

			binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
				.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

			binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

			binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

			binding.nameTextView.text = item.title

			binding.timeTextView.text = item.createdAt.orEmpty()

			binding.placeTextView.text = "${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}"

			binding.storeImageImageView.setupWithGlide {
				load(item.store?.image)
			}

			binding.storeTextView.text = item.store?.name

			binding.priceTextView.text = item.price?.round(2).orZero().toString()
			binding.currencyTextView.text = item.country?.currency.orEmpty()

			binding.negotiableTextView.isVisible = item.isNegotiable
		}
	}
}
