package ma.ya.cometchatintegration.extensions

import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
	// Get the InputMethodManager
	val inputMethodManager = activity?.getSystemService<InputMethodManager>() ?: return

	// Hide the keyboard
	inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

	/*
	var view = activity.currentFocus
  if (view == null) view = View(activity)
  val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0)
	 */
}
