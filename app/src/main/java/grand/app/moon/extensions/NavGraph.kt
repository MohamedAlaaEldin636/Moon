package grand.app.moon.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafely(navDirections: NavDirections) =
	kotlin.runCatching { navigate(navDirections) }
