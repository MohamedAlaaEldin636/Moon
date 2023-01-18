package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.extensions.app
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.myAds.MyAdOrShopPackageFragmentArgs
import grand.app.moon.presentation.myAds.MyAdOrShopPackageFragmentDirections
import javax.inject.Inject

@HiltViewModel
class MyAdOrShopPackageViewModel @Inject constructor(
	application: Application,
	val repoPackages: RepositoryPackages,
	val args: MyAdOrShopPackageFragmentArgs,
) : AndroidViewModel(application) {

	val response = MutableLiveData<ResponsePackage?>()

	val number = response.map {
		if (args.isAdvNotShop) {
			app.getString(R.string.var_ads, it?.advertisementsCount.orZero().toString())
		}else {
			app.getString(R.string.shop_promotion_8)
		}
	}

	val duration = response.map {
		val typeOfPeriod = it?.typePeriod?.stringRes?.let { app.getString(it) }.orEmpty()
		val period = "${it?.period.orZero()} $typeOfPeriod"
		app.getString(R.string.for_var, period)
	}

	val price = response.map {
		"${it?.price.orZero()} ${it?.country?.currency.orEmpty()}"
	}

	val restDays = response.map {
		app.getString(R.string.var_days, it?.restDays.orZero().toString())
	}

	fun renewPackageOrSubscribeToNewOne(view: View) {
		view.findNavController().navigateSafely(
			MyAdOrShopPackageFragmentDirections.actionDestMyAdOrShopPackageToDestMakeAdvOrStorePremium(args.advertisementId)
		)
	}

}
