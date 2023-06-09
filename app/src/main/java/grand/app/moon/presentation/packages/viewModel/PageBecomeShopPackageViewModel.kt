package grand.app.moon.presentation.packages.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.GoSellSDKUtils2
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.extensions.*
import grand.app.moon.presentation.packages.BecomeShopPackagesFragment
import grand.app.moon.presentation.packages.BecomeShopPackagesFragmentDirections
import grand.app.moon.presentation.packages.PageBecomeShopPackageFragment
import javax.inject.Inject

@HiltViewModel
class PageBecomeShopPackageViewModel @Inject constructor(
	application: Application,
	private val userLocalUseCase: UserLocalUseCase,
	private val repositoryPackages: RepositoryPackages,
	val repoShop: RepoShop,
	val appPreferences: AppPreferences,
) : AndroidViewModel(application) {

	companion object {
		const val META_DATA_PACKAGE_ID = "grand.app.moon.presentation.packages.viewModel.PageBecomeShopPackageViewModel.META_DATA_PACKAGE_ID"
	}

	val response = MutableLiveData<ResponsePackage?>()

	val title = response.map {
		it?.title.orEmpty()
	}

	val period = response.map {
		val type = it?.typePeriod?.stringRes?.let { res ->
			app.getString(res)
		}.orEmpty()

		"${it?.period.orZero()} $type"
	}

	val price = response.map {
		"${it?.price.orZero().toIntIfNoFractionsOrThisFloat()} ${it?.country?.currency.orEmpty()}"
	}

	val adsCount = response.map {
		app.getString(R.string.ads_count_var, it?.advertisementsCount.orZero().toString())
	}

	val storiesCount = response.map {
		app.getString(R.string.addintg_var_stories, it?.storiesCount.orZero().toString())
	}

	val exploreCount = response.map {
		app.getString(R.string.addintg_var_explore, it?.exploresCount.orZero().toString())
	}

	val statsCount = response.map {
		app.getString(R.string.checking_statistics)
	}

	val adsTooltip = response.map {
		it?.advertisementsTooltip.orEmpty()
	}

	val storiesTooltip = response.map {
		it?.storiesTooltip.orEmpty()
	}

	val exploreTooltip = response.map {
		it?.exploresTooltip.orEmpty()
	}

	val statsTooltip = response.map {
		it?.statisticsTooltip.orEmpty()
	}

	val showAdsTooltip = MutableLiveData(false)
	val showStoriesTooltip = MutableLiveData(false)
	val showExploreTooltip = MutableLiveData(false)
	val showStatsViewsTooltip = MutableLiveData(false)

	val isPopular = response.map {
		it?.isPopular.orFalse()
	}

	private val isSubscribed = response.map {
		it?.isSubscribed.orFalse()
	}

	val buttonText = switchMapMultiple2(isSubscribed, response) {
		val res = when {
			isSubscribed.value == true && response.value?.restDays.orZero() > 0 -> {
				R.string.already_subscribed
			}
			isSubscribed.value == true -> {
				R.string.renew_package
			}
			else -> {
				R.string.subscribe_now
			}
		}

		app.getString(res)
	}

	val remainingDaysInPackageText = response.map {
		if (it?.isSubscribed.orFalse()) {
			app.getString(R.string.your_subscription_will_end_in_var_days, it?.restDays.orZero().toString())
		}else {
			""
		}
	}

	fun performShowAdsTooltip() {
		clearTooltips()
		showAdsTooltip.postValue(true)
	}
	fun performShowStoriesTooltip() {
		clearTooltips()
		showStoriesTooltip.postValue(true)
	}
	fun performShowExploreTooltip() {
		clearTooltips()
		showExploreTooltip.postValue(true)
	}
	fun performShowStatsViewsTooltip() {
		clearTooltips()
		showStatsViewsTooltip.postValue(true)
	}

	fun clearTooltips() {
		showAdsTooltip.value = false
		showStoriesTooltip.value = false
		showExploreTooltip.value = false
		showStatsViewsTooltip.value = false
	}

	fun subscribeNowOrCheckCurrentSubscription(view: View) {
		val fragment = view.findFragmentOrNull<PageBecomeShopPackageFragment>() ?: return

		val navController = fragment.findNavController()

		if (isSubscribed.value == true && response.value?.restDays.orZero() > 0) {
			// Check Subscription
			fragment.findNavController().navigateSafely(
				BecomeShopPackagesFragmentDirections.actionDestBecomeShopPackagesToDestMyBecomeShopPackage()
			)
		}else {
			// Subscribe Now to new package or renew current package and return success isa.
			val currency = repoShop.getSelectedCountry()?.englishCurrencyIsoCode ?: return
			if (currency.isEmpty()) return

			GoSellSDKUtils2.startPayment(
				fragment,
				appPreferences,
				currency,
				response.value?.price.orZero().toBigDecimal(),
				userLocalUseCase,
				mutableMapOf(
					META_DATA_PACKAGE_ID to response.value?.id.orZero().toString(),
				).toHashMap(),
			) { onSuccessAction ->
				fragment.handleRetryAbleActionCancellableNullable(
					action = {
						repositoryPackages.subscribeToBecomeShopPackage(response.value?.id.orZero())
					}
				) { successResponse ->
					onSuccessAction()

					val user = userLocalUseCase()
					userLocalUseCase(user.copy(isStore = true))

					navController.setResultInPreviousBackStackEntrySavedStateHandleViaGson(
						true // subscribed successfully
					)

					// Change Data
					response.value = response.value?.copy(
						isSubscribed = true,
						restDays = response.value?.getPeriodInDays().orZero()
					)

					val parentFragment = (fragment.parentFragment as? BecomeShopPackagesFragment)

					val parentViewModel = parentFragment?.viewModel
					parentViewModel?.allPackages?.forEach {
						if (it.id == response.value?.id) {
							it.isSubscribed = true
							it.restDays = response.value?.getPeriodInDays().orZero()
						}else {
							it.isSubscribed = false
						}
					}

					val willGoToCreateShopNotMyPackage = if (successResponse?.storeInfoIsCompleted == true) {
						// Check Subscription
						navController.navigateSafely(
							BecomeShopPackagesFragmentDirections.actionDestBecomeShopPackagesToDestMyBecomeShopPackage()
						)

						false
					}else {
						// Now go to create store data and on result of it's creation nav up.
						parentFragment?.observeCreateShopFragment()

						navController.navigateSafely(
							BecomeShopPackagesFragmentDirections.actionDestBecomeShopPackagesToDestCreateStore()
						)

						true
					}

					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.success.shop.subscription",
						paths = arrayOf(willGoToCreateShopNotMyPackage.toString())
					)
				}
			}
		}
	}

}
