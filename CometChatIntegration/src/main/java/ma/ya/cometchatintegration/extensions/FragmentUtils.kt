package ma.ya.cometchatintegration.extensions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.*

inline fun <reified F : Fragment> View.findFragmentOrNull(): F? {
	return try {
		findFragment()
	}catch (e: IllegalStateException) {
		null
	}
}

inline fun <reified T> Fragment.setFragmentResultListenerUsingJson(
	key: String,
	crossinline onResult: (value: T) -> Unit
) {
	setFragmentResultListener(key) { _, bundle ->
		val value = bundle.getString(GsonUtils.GSON_KEY).fromJsonInlinedOrNull<T>()

		clearFragmentResultListener(key)

		if (value != null) {
			onResult(value)
		}
	}
}

inline fun <reified T> Fragment.setFragmentResultUsingJson(key: String, value: T) {
	setFragmentResult(
		key,
		bundleOf(GsonUtils.GSON_KEY to value.toJsonInlinedOrNull().orEmpty())
	)
}

inline fun <reified C : DialogFragment> Fragment.showDialogFragment(args: Bundle = Bundle.EMPTY) {
	val javaClassName = C::class.java.name

	val dialogFragment = kotlin.runCatching {
		Class.forName(javaClassName).newInstance() as DialogFragment
	}.getOrNull() ?: return

	dialogFragment.arguments = args

	dialogFragment.show(childFragmentManager, javaClassName)
}
