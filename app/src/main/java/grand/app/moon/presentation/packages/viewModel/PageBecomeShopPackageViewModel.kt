package grand.app.moon.presentation.packages.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.packages.BecomeShopPackagesFragment
import grand.app.moon.presentation.packages.BecomeShopPackagesFragmentDirections
import grand.app.moon.presentation.packages.PageBecomeShopPackageFragment
import javax.inject.Inject

@HiltViewModel
class PageBecomeShopPackageViewModel @Inject constructor(
	application: Application,
	private val userLocalUseCase: UserLocalUseCase,
	private val repositoryPackages: RepositoryPackages,
) : AndroidViewModel(application) {

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
		"${it?.price.orZero()} ${it?.country?.currency.orEmpty()}"
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
		app.getString(R.string.checking_statistics_var, it?.statisticsViewsCount.orZero().toString())
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
			fragment.handleRetryAbleActionCancellableNullable(
				action = {
					repositoryPackages.subscribeToBecomeShopPackage(response.value?.id.orZero())
				}
			) { successResponse ->
				fragment.showMessage(fragment.getString(R.string.done_successfully))

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
				parentViewModel?.allPackages?.firstOrNull {
					it.id == response.value?.id
				}?.also {
					it.isSubscribed = true
					it.restDays = response.value?.getPeriodInDays().orZero()
				}

				if (successResponse?.storeInfoIsCompleted == true) {
					// Check Subscription
					navController.navigateSafely(
						BecomeShopPackagesFragmentDirections.actionDestBecomeShopPackagesToDestMyBecomeShopPackage()
					)
				}else {
					// Now go to create store data and on result of it's creation nav up.
					parentFragment?.observeCreateShopFragment()

					navController.navigateSafely(
						BecomeShopPackagesFragmentDirections.actionDestBecomeShopPackagesToDestCreateStore()
					)
				}
			}
		}
	}

}
