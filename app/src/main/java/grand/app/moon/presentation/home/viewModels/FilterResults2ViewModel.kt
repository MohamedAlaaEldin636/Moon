package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvAdvBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterResults2ViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val adapter = RVPagingItemCommonListUsage<ItemHomeRvAdvBinding, ItemAdvertisementInResponseHome>(
		R.layout.item_home_rv_adv,
		onItemClick = { _, binding ->
			val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemAdvertisementInResponseHome>()
				?: return@RVPagingItemCommonListUsage

			if (item.store?.id == userLocalUseCase().id) {
				binding.root.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
					paths = arrayOf(item.id.orZero().toString())
				)
			}else {
				binding.root.findNavController().navigate(
					R.id.nav_ads, bundleOf(
						"id" to item.id.orZero(),
						"type" to 2,
						"from_store" to false
					)
				)
			}
		},
		additionalListenersSetups = { adapter, binding ->
			val listener = View.OnClickListener { view ->
				//val context = binding.root.context ?: return@OnClickListener
				val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
					?: return@OnClickListener
				//val position = binding.linearLayout.tag as? Int ?: return@OnClickListener

				if (item.store?.id == userLocalUseCase().id) {
					view.findNavController().navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.create.store"
					)
				}else {
					view.findNavController().navigate(
						R.id.nav_store,
						bundleOf(
							"id" to item.store?.id.orZero(),
							"type" to 3
						), Constants.NAVIGATION_OPTIONS
					)
				}
			}
			binding.storeImageImageView.setOnClickListener(listener)
			binding.storeTextView.setOnClickListener(listener)
			binding.favImageView.setOnClickListener {
				val context = binding.root.context ?: return@setOnClickListener
				val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
					?: return@setOnClickListener
				val position = binding.linearLayout.tag as? Int ?: return@setOnClickListener

				adapter.updateItem(position) {
					it.isFavorite = item.isFavorite.orFalse().not()
				}
				context.applicationScope?.launch {
					repoShop.favOrUnFavAdv(item.id.orZero())
				}
			}
			binding.whatsAppImageView.setOnClickListener {
				val context = binding.root.context ?: return@setOnClickListener
				val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
					?: return@setOnClickListener
				//val position = binding.linearLayout.tag as? Int ?: return@setOnClickListener

				context.launchWhatsApp(item.phone.orEmpty())
			}
			binding.callImageView.setOnClickListener {
				val context = binding.root.context ?: return@setOnClickListener
				val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
					?: return@setOnClickListener
				//val position = binding.linearLayout.tag as? Int ?: return@setOnClickListener

				context.launchDialNumber(item.phone.orEmpty())
			}
			binding.chatImageView.setOnClickListener { view ->
				val context = binding.root.context ?: return@setOnClickListener
				val item = binding.root.getTagJson<ItemAdvertisementInResponseHome>()
					?: return@setOnClickListener
				//val position = binding.linearLayout.tag as? Int ?: return@setOnClickListener

				if (context.isLoginWithOpenAuth()) {
					item.store?.also {
						context.openChatStore(view, it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
					}
				}
			}
		}
	) { binding, position, item ->
		binding.root.setTagJson(item)
		binding.linearLayout.tag = position

		binding.imageImageView.setupWithGlide {
			load(item.image)
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
			load(item.store?.image)
		}

		binding.storeTextView.text = item.store?.name

		binding.priceTextView.text = "${item.price?.round(2).orZero()} ${item.country?.currency.orEmpty()}"

		binding.negotiableTextView.isVisible = item.isNegotiable
	}

}
