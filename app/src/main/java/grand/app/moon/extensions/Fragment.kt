package grand.app.moon.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment

inline fun <reified F : Fragment> View.findFragmentOrNull(): F? {
	return try {
		findFragment()
	}catch (e: IllegalStateException) {
		null
	}
}
