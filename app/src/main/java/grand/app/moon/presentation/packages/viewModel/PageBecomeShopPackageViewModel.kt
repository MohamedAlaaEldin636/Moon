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
import grand.app.moon.domain.packages.ResponseBecomeShopPackage
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

	val response = MutableLiveData<ResponseBecomeShopPackage?>()

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

	val buttonText = isSubscribed.map {
		app.getString(
			if (it) R.string.already_subscribed else R.string.subscribe_now
		)
	}

	val remainingDaysInPackageText = response.map {
		if (it?.isSubscribed.orFalse()) {
			app.getString(R.string.your_subscription_will_end_in_var_days, it?.restDays.orZero().toString())
		}else {
			""
		}
	}

	fun performShowAdsTooltip() = showAdsTooltip.postValue(true)
	fun performShowStoriesTooltip() = showStoriesTooltip.postValue(true)
	fun performShowExploreTooltip() = showExploreTooltip.postValue(true)
	fun performShowStatsViewsTooltip() = showStatsViewsTooltip.postValue(true)

	fun clearTooltips() {
		showAdsTooltip.value = false
		showStoriesTooltip.value = false
		showExploreTooltip.value = false
		showStatsViewsTooltip.value = false
	}

	fun subscribeNowOrCheckCurrentSubscription(view: View) {
		val fragment = view.findFragmentOrNull<PageBecomeShopPackageFragment>() ?: return

		val navController = fragment.findNavController()

		if (isSubscribed.value == true) {
			// Check Subscription
			fragment.findNavController().navigateSafely(
				BecomeShopPackagesFragmentDirections.actionDestBecomeShopPackagesToDestMyBecomeShopPackage()
			)
		}else {
			// Subscribe Now and return success isa.
			fragment.handleRetryAbleActionCancellableNullable(
				action = {
					repositoryPackages.subscribeToBecomeShopPackage(response.value?.id.orZero())
				}
			) {
				fragment.showMessage(fragment.getString(R.string.done_successfully))

				val user = userLocalUseCase()
				userLocalUseCase(user.copy(isStore = true))

				navController.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
					true // subscribed successfully
				)
			}
		}
	}

}
