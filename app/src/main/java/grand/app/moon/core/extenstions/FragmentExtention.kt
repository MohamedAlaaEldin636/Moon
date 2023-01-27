package grand.app.moon.core.extenstions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController

fun Fragment.rateApp(){
  val uri = Uri.parse("market://details?id=${requireActivity().packageName}")
  val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
  try {
    startActivity(myAppLinkToMarket)
  } catch (e: ActivityNotFoundException) {
    Toast.makeText(requireContext(), "Impossible to find an application for the market", Toast.LENGTH_LONG).show()
  }

}

fun <T> Fragment.actOnGetIfNotInitialValueOrGetLiveData(
  key: String,
  initialValue: T,
  owner: LifecycleOwner,
  conditionOnResultToSetBackToInitialValue: (T?) -> Boolean,
  actionWhenConditionIsMet: (T?) -> Unit
) = findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
  key, initialValue, owner, conditionOnResultToSetBackToInitialValue, actionWhenConditionIsMet
)

fun <T> SavedStateHandle.actOnGetIfNotInitialValueOrGetLiveData(
  key: String,
  initialValue: T,
  owner: LifecycleOwner,
  conditionOnResultToSetBackToInitialValue: (T?) -> Boolean,
  actionWhenConditionIsMet: (T?) -> Unit
) {
  val currentValue = get<T>(key)

  if (contains(key) && currentValue != initialValue) {
    remove<T>(key)

    actionWhenConditionIsMet(currentValue)
  }else {
    getLiveData(
      key,
      initialValue
    ).observe(owner) {
      if (conditionOnResultToSetBackToInitialValue(it)) {
        remove<T>(key)

        actionWhenConditionIsMet(it)
      }
    }
  }
}
