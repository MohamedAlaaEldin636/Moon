package grand.app.moon.extensions

import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.utils.Resource
import kotlinx.coroutines.delay

suspend fun suspendUntilNotNull(delayMillis: Long = 50L, value: () -> Any?) {
	while (value() == null) {
		delay(delayMillis)
	}
}

infix fun <A, B, C> Pair<A, B>.triple(third: C): Triple<A, B, C> = Triple(first, second, third)

fun View.showSnackbarWithAction(
	failure: Resource.Failure,
	duration: Int = Snackbar.LENGTH_INDEFINITE,
	actionText: String = context?.getString(R.string.retry).orEmpty(),
	action: () -> Unit
) = showSnackbar(failure, duration, actionText, action)

fun View.showSnackbar(
	failure: Resource.Failure,
	duration: Int = Snackbar.LENGTH_SHORT,
	actionText: String? = null, // getString(R.string.retry)
	action: (() -> Unit)? = null
) = context.apply {
	if (this == null) return@apply

	val errorMsg = (if (failure.message.isNullOrEmpty()) {
		getString(R.string.something_went_wrong_please_try_again)
	}else {
		failure.message
	}) + " - ${failure.failureStatus}"

	Log.e("sas", "sas -> $errorMsg")

	Snackbar.make(this@showSnackbar, errorMsg, duration)
		.let {
			if (action == null || actionText.isNullOrEmpty()) it else {
				it.setAction(actionText) {
					action()
				}
			}
		}
		.show()
}
/*
private fun showSnackbarWithRetry(isCategoriesNotCountriesError: Boolean, failure: Resource.Failure) {
		val errorMsg = (if (failure.message.isNullOrEmpty()) {
			getString(R.string.something_went_wrong_please_try_again)
		}else {
			failure.message
		}) + " - ${failure.failureStatus}"

		Log.e("sas", "sas -> $errorMsg")

		Snackbar.make(binding.relativeLayout, errorMsg, Snackbar.LENGTH_INDEFINITE)
			.setAction(getString(R.string.retry)) {
				if (isCategoriesNotCountriesError) {
					viewModel.homeCategories()
				}else {
					viewModel.homeCountries()
				}
			}
			.show()
	}
 */

object General {
	fun TODO(any: Any?) {
		Toast.makeText(MyApplication.instance, any.toString(), Toast.LENGTH_SHORT).show()
	}
}
