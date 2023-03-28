package grand.app.moon.presentation.home.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSearchResultBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrDefaultUserBA
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsInteractedWithByOtherUsersViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
	val args: AdsInteractedWithByOtherUsersFragmentArgs
) : AndroidViewModel(application) {

	val showWholePageLoader = MutableLiveData(true)

	val showLabelText = when (args.type) {
		AdsInteractedWithByOtherUsersFragment.Type.LAST_VIEWED -> true
		AdsInteractedWithByOtherUsersFragment.Type.LAST_SEARCHED,
		AdsInteractedWithByOtherUsersFragment.Type.FAV -> false
	}
	val count = MutableLiveData<Int?>()
	val textOfLabel = count.map {
		app.getString(R.string.num_ads_watched, it.orZero())
	}

	val ads = when (args.type) {
		AdsInteractedWithByOtherUsersFragment.Type.LAST_VIEWED -> repoShop.getLastViewedAds()
		AdsInteractedWithByOtherUsersFragment.Type.LAST_SEARCHED -> repoShop.getLastSearchedAds()
		AdsInteractedWithByOtherUsersFragment.Type.FAV -> repoShop.getFavByOtherUsersAds()
	}

	@SuppressLint("SetTextI18n")
	val adapter = RVPagingItemCommonListUsage<ItemSearchResultBinding, ItemAdvertisementInResponseHome>(
		R.layout.item_search_result,
		onItemClick = { _, binding ->
			val context = binding.root.context ?: return@RVPagingItemCommonListUsage

			val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
				?: return@RVPagingItemCommonListUsage

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
					repoShop.interactionForAdWhatsApp(item.store?.id.orZero())
				}

				context.launchWhatsApp(item.store?.fullWhatsAppPhone.orEmpty())
			}
			binding.phoneConstraintLayout.setOnClickListener {
				val context = binding.root.context ?: return@setOnClickListener

				val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
					?: return@setOnClickListener

				context.applicationScope?.launch {
					repoShop.interactionForAdCall(item.store?.id.orZero())
				}

				context.launchDialNumber(item.store?.fullAdsPhone.orEmpty())
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
	) { binding, position, item ->
		binding.root.setTagJson(item)
		binding.root.setTag(R.id.position_tag, position)

		binding.imageImageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
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

		binding.storeImageImageView.setupWithGlideOrDefaultUserBA(item.store?.image)

		binding.storeTextView.text = item.store?.name

		binding.priceTextView.text = item.price?.round(2).orZero().toString()
		binding.currencyTextView.text = item.country?.currency.orEmpty()

		binding.negotiableTextView.isVisible = item.isNegotiable
	}

}
