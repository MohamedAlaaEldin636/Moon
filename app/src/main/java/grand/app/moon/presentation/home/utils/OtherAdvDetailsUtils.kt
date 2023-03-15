package grand.app.moon.presentation.home.utils

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.databinding.ItemHomeRvAdvBinding
import grand.app.moon.databinding.ItemHomeRvStoreBinding
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.extensions.bindingAdapter.setCompoundDrawablesRelativeWithIntrinsicBoundsStart
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrDefaultUserBA
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome
import grand.app.moon.presentation.home.viewModels.OtherAdvDetailsViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun OtherAdvDetailsViewModel.getAdapterForStores() = RVItemCommonListUsage<ItemHomeRvStoreBinding, ItemStoreInResponseHome>(
	R.layout.item_home_rv_store,
	onItemClick = { _, binding ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@RVItemCommonListUsage

		userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
			context,
			binding.root.findNavController(),
			item
		)
	},
	additionalListenersSetups = { adapter, binding ->
		binding.followingButtonView.setOnClickListener { view ->
			val context = view.context ?: return@setOnClickListener

			val item = binding.root.getTagJson<ItemStoreInResponseHome>() ?: return@setOnClickListener
			val position = binding.root.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener

			if (context.isLoginWithOpenAuth()) {
				context.applicationScope?.launch {
					repoShop.followStore(item.id.orZero())
				}

				adapter.updateItem(
					position,
					item.copy(isFollowing = item.isFollowing.orFalse().not())
				)
			}
		}
	}
) { binding, position, item ->
	binding.root.tag = item.toJsonInlinedOrNull()
	binding.root.setTag(R.id.position_tag, position)

	val context = binding.root.context ?: return@RVItemCommonListUsage

	binding.imageImageView.setupWithGlide {
		load(item.image).saveDiskCacheStrategyAll()
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

fun OtherAdvDetailsViewModel.getAdapterForAds() = RVItemCommonListUsage<ItemHomeRvAdvBinding, ItemAdvertisementInResponseHome>(
	R.layout.item_home_rv_adv,
	onItemClick = { _, binding ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
			?: return@RVItemCommonListUsage

		userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
			context,
			binding.root.findNavController(),
			item
		)
	},
	additionalListenersSetups = { adapter, binding ->
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
					item.copy(isFavorite = item.isFavorite.orFalse().not())
				)
			}
		}

		binding.whatsAppImageView.setOnClickListener {
			val context = binding.root.context ?: return@setOnClickListener

			val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
				?: return@setOnClickListener

			context.applicationScope?.launch {
				repoShop.interactionForAdWhatsApp(item.id.orZero())
			}

			context.launchWhatsApp(item.store?.fullWhatsAppPhone.orEmpty())
		}
		binding.callImageView.setOnClickListener {
			val context = binding.root.context ?: return@setOnClickListener

			val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
				?: return@setOnClickListener

			context.applicationScope?.launch {
				repoShop.interactionForAdCall(item.id.orZero())
			}

			context.launchDialNumber("${item.country?.countryCode.orEmpty()} ${item.phone.orEmpty()}")
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
) { binding, position, item ->
	binding.root.setTagJson(item)
	binding.root.setTag(R.id.position_tag, position)

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

	binding.storeImageImageView.setupWithGlideOrDefaultUserBA(item.store?.image)

	binding.storeTextView.text = item.store?.name

	binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

	binding.negotiableTextView.isVisible = item.isNegotiable
}
