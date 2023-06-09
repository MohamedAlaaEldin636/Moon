package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.GoSellSDKUtils2
import grand.app.moon.core.extenstions.toast
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemPackageInPackagesBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.adjustInsideRV
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.base.utils.showMessage
import grand.app.moon.presentation.myAds.MakeAdvOrStorePremiumFragment
import grand.app.moon.presentation.myAds.MakeAdvOrStorePremiumFragmentArgs
import grand.app.moon.presentation.myAds.MakeAdvOrStorePremiumFragmentDirections
import grand.app.moon.presentation.myAds.MyAdsFragment
import grand.app.moon.presentation.myAds.utils.ISessionDelegate
import grand.app.moon.presentation.myAds.utils.startPayment
import javax.inject.Inject

@HiltViewModel
class MakeAdvOrStorePremiumViewModel @Inject constructor(
	application: Application,
	private val repoPackages: RepositoryPackages,
	private val args: MakeAdvOrStorePremiumFragmentArgs,
	val userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
	private val appPreferences: AppPreferences,
) : AndroidViewModel(application) {

	companion object {
		const val META_DATA_PACKAGE_ID = "META_DATA_PACKAGE_ID"
		const val META_DATA_PACKAGE_IS_AD_NOT_STORE = "META_DATA_PACKAGE_IS_AD_NOT_STORE"
		const val META_DATA_PACKAGE_AD_ID_OR_EMPTY = "META_DATA_PACKAGE_AD_ID_OR_EMPTY"
	}

	private val isStore by lazy {
		userLocalUseCase().isStore.orFalse()
	}

	val adsNotStoresAreSelected = MutableLiveData(true)

	val selectedAdsPackageId = MutableLiveData<Int?>()
	val selectedShopsPackageId = MutableLiveData<Int?>()

	val adsToggleTextColorRes = adsNotStoresAreSelected.map {
		if (it == true) R.color.white else R.color.colorPrimary
	}
	val storeToggleTextColorRes = adsNotStoresAreSelected.map {
		if (it != true) R.color.white else R.color.colorPrimary
	}

	val buttonText = switchMapMultiple2(adsNotStoresAreSelected, selectedAdsPackageId, selectedShopsPackageId) {
		val selectedId = if (adsNotStoresAreSelected.value == true) selectedAdsPackageId.value else selectedShopsPackageId.value
		val selectedPackage = getCurrentlyUsedAdapter().snapshot().firstOrNull { it?.id == selectedId }

		when {
			selectedPackage == null -> app.getString(R.string.pick_your_package_now)
			selectedPackage.isSubscribed == true -> if (selectedPackage.restDays.orZero() > 0) {
				app.getString(R.string.check_ad_premium)
			}else {
				app.getString(R.string.renew_package)
			}
			else -> app.getString(R.string.promote_and_pay_var_var, selectedPackage.price.orZero().toIntIfNoFractionsOrThisFloat(), selectedPackage.country?.currency.orEmpty())
		}
	}

	val adsPackages = repoPackages.getAdsPromotionPackagesPaging()
	val shopsPackages = repoPackages.getShopsPromotionPackagesPaging()

	val adapterForAds = RVAdapterPromotionPackages(true)
	val adapterForShops = RVAdapterPromotionPackages(false)

	fun toggleAdsOrStores(view: View, showAds: Boolean) {
		val context = view.context ?: return

		if (showAds.not() && isStore.not()) {
			return showMessage(context, context.getString(R.string.available_only_for_store_account))
		}

		val binding = view.findFragmentOrNull<MakeAdvOrStorePremiumFragment>()?.binding ?: return

		if (adsNotStoresAreSelected.value == showAds) return

		adsNotStoresAreSelected.value = showAds

		binding.recyclerView.adapter = getCurrentlyUsedAdapter()
	}

	fun renewPackageOrSubscribeToNewOne(view: View) {
		val fragment = view.findFragmentOrNull<MakeAdvOrStorePremiumFragment>() ?: return

		val selectedIdOfPackage = if (adsNotStoresAreSelected.value == true) selectedAdsPackageId.value else selectedShopsPackageId.value
		val selectedPackage = getCurrentlyUsedAdapter().snapshot().firstOrNull { it?.id == selectedIdOfPackage }

		if (selectedIdOfPackage == null || selectedPackage == null) {
			return fragment.showMessage(fragment.getString(R.string.please_select_package))
		}

		if (selectedPackage.isSubscribed == true && selectedPackage.restDays.orZero() > 0) {
			fragment.findNavController().navigateSafely(
				MakeAdvOrStorePremiumFragmentDirections.actionDestMakeAdvOrStorePremiumToDestMyAdOrShopPackage(
					args.advertisementId, adsNotStoresAreSelected.value.orFalse()
				)
			)
		}else {
			// Either wasn't subscribed OR renewing package
			val currency = repoShop.getSelectedCountry()?.englishCurrencyIsoCode ?: return
			if (currency.isEmpty()) return

			GoSellSDKUtils2.startPayment(
				fragment,
				appPreferences,
				currency,
				selectedPackage.price.orZero().toBigDecimal(),
				userLocalUseCase,
				mutableMapOf(
					META_DATA_PACKAGE_ID to selectedIdOfPackage.toString(),
					META_DATA_PACKAGE_IS_AD_NOT_STORE to (adsNotStoresAreSelected.value == true).toString(),
					META_DATA_PACKAGE_AD_ID_OR_EMPTY to (if (adsNotStoresAreSelected.value == true) args.advertisementId else null).toString(),
				).toHashMap(),
			) { onSuccessAction ->
				fragment.handleRetryAbleActionCancellableNullable(
					action = {
						if (adsNotStoresAreSelected.value == true) {
							repoPackages.subscribeToMakeAdvertisementPremiumPackage(args.advertisementId, selectedIdOfPackage)
						}else {
							repoPackages.subscribeToMakeShopPremiumPackage(selectedIdOfPackage)
						}
					}
				) {
					onSuccessAction()

					fragment.showMessage(fragment.getString(R.string.done_successfully))

					getCurrentlyUsedAdapter().snapshot().forEach {
						if (it?.id == selectedIdOfPackage) {
							it.isSubscribed = true
							it.restDays = selectedPackage.getPeriodInDays()
						}else {
							it?.isSubscribed = false
						}
					}

					if (adsNotStoresAreSelected.value == true) {
						selectedAdsPackageId.value = null
						selectedAdsPackageId.postValue(selectedIdOfPackage)

						fragment.findNavController().setResultInPreviousBackStackEntrySavedStateHandleViaGson(
							MyAdsFragment.NewAdvertisementState.BECAME_PREMIUM
						)
					}else {
						selectedShopsPackageId.value = null
						selectedShopsPackageId.postValue(selectedIdOfPackage)
					}
				}
			}
		}
	}

	fun getCurrentlyUsedAdapter() : RVAdapterPromotionPackages {
		return if (adsNotStoresAreSelected.value == true) adapterForAds else adapterForShops
	}

	inner class RVAdapterPromotionPackages(
		private val isForAdsNotShops: Boolean,
	) : RVPagingItemCommonListUsage<ItemPackageInPackagesBinding, ResponsePackage>(
		R.layout.item_package_in_packages,
		onItemClick = { adapter, binding ->
			apply {
				val previousSelectionPosition = adapter.snapshot().indexOfFirstOrNull {
					it?.id == if (isForAdsNotShops) {
						selectedAdsPackageId.value
					}else {
						selectedShopsPackageId.value
					}
				}

				val newSelectionPosition = binding.constraintLayout.tag as? Int ?: return@apply

				if (previousSelectionPosition == newSelectionPosition) return@apply

				val newSelectionId = binding.innerConstraintLayout.tag as? Int ?: return@apply
				if (isForAdsNotShops) {
					selectedAdsPackageId.value = newSelectionId
				}else {
					selectedShopsPackageId.value = newSelectionId
				}

				MyLogger.e("selectedAdsPackageId.value selectedShopsPackageId.value ch 0 ${selectedAdsPackageId.value} ${selectedShopsPackageId.value}")

				adapter.notifyItemsChanged(previousSelectionPosition, newSelectionPosition)
			}
		},
		onBind = { binding, position, item ->
			MyLogger.e("selectedAdsPackageId.value selectedShopsPackageId.value ch 1 ${selectedAdsPackageId.value} ${selectedShopsPackageId.value}")

			apply {
				val context = binding.root.context ?: return@apply

				binding.constraintLayout.tag = position
				binding.innerConstraintLayout.tag = item.id

				binding.priceTextView.adjustInsideRV(
					"${item.price.orZero().toIntIfNoFractionsOrThisFloat()} ${item.country?.currency.orEmpty()}",
					20f,
					6f
				)

				val typeOfPeriod = item.typePeriod?.getStringWithQuantity(app, item.period.orZero()).orEmpty()
				val period = "${item.period.orZero()} $typeOfPeriod"
				binding.durationTextView.adjustInsideRV(
					app.getString(R.string.for_var, period),
					12f,
					6f
				)

				binding.numberTextView.adjustInsideRV(
					if (isForAdsNotShops) {
						val count = item.advertisementsCount.orZero()
						app.resources.getQuantityString(R.plurals.var_ads, count, count)
					}else {
						app.getString(R.string.shop_promotion_8)
					},
					13f,
					6f
				)

				if (item.id == if (isForAdsNotShops) selectedAdsPackageId.value else selectedShopsPackageId.value) {
					binding.innerConstraintLayout.background = context.getDrawableOrTransparent(R.drawable.dr_rect_9)
					binding.priceTextView.setTextColor(Color.WHITE)
					binding.durationTextView.setTextColor(Color.WHITE)
				}else {
					binding.innerConstraintLayout.background = context.getDrawableOrTransparent(R.drawable.dr_rect_9_with_border)
					val color = ContextCompat.getColor(context, R.color.colorPrimary)
					binding.priceTextView.setTextColor(color)
					binding.durationTextView.setTextColor(color)
				}
			}
		}
	)

}
