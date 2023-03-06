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
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.extensions.*
import grand.app.moon.presentation.packages.MyBecomeShopPackageFragment
import javax.inject.Inject

@HiltViewModel
class MyBecomeShopPackageViewModel @Inject constructor(
	application: Application,
	val repoPackages: RepositoryPackages,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponsePackage?>()

	val name = response.map {
		it?.title.orEmpty()
	}

	val duration = response.map { response ->
		val typeOfPeriod = response?.typePeriod?.stringRes?.let { app.getString(it) }.orEmpty()
		val period = "${response?.period.orZero()} $typeOfPeriod"
		app.getString(R.string.for_var, period)
	}

	val price = response.map {
		"${it?.price.orZero().toIntIfNoFractionsOrThisFloat()} ${it?.country?.currency.orEmpty()}"
	}

	val restDays = response.map {
		app.getString(R.string.var_days, it?.restDays.orZero().toString())
	}

	val remainingAds = response.map {
		"${it?.advertisementsCount.orZero()} ${app.getString(R.string.ads)}"
	}

	val remainingStories = response.map {
		"${it?.storiesCount.orZero()} ${app.getString(R.string.stories)}"
	}

	val remainingExplore = response.map {
		"${it?.exploresCount.orZero()} ${app.getString(R.string.explore)}"
	}

	fun renewOrSubscribeToNewPackage(view: View) {
		val fragment = view.findFragmentOrNull<MyBecomeShopPackageFragment>() ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.become.shop.packages"
		)
	}

}
