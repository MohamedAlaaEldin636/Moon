package grand.app.moon.presentation.myAds.model

import androidx.annotation.StringRes
import grand.app.moon.R

enum class TypeOfAd(val apiValue: Int?, @StringRes val stringRes: Int) {
	ALL(null, R.string.all_ads_3),
	FREE(2, R.string.free_ads),
	PREMIUM(1, R.string.premium_ads)
}
