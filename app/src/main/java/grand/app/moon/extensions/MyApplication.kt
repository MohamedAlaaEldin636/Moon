package grand.app.moon.extensions

import android.content.Context
import grand.app.moon.core.MyApplication
import grand.app.moon.presentation.home.models.ItemStoreInResponseHome

val Context?.applicationScope get() = (this?.applicationContext as? MyApplication)?.applicationScope

val Context?.myApplication get() = this?.applicationContext as? MyApplication

/** @param item after follow state change */
fun Context?.followOrUnFollowStoreFromNotHomeScreen(item: ItemStoreInResponseHome?) {
	if (item != null) {
		myApplication?.followOrUnFollowStoreFromNotHomeScreen(item)
	}
}

fun Context?.getStoresFollowingStateChangesOnceTillNewChangesCome() =
	myApplication?.getStoresFollowingStateChangesOnceTillNewChangesCome().orEmpty()
/*

fun Context?.followOrUnFollowStoreFromNotHomeScreen(id: Int, followNotUnFollow: Boolean) =
	myApplication?.followOrUnFollowStoreFromNotHomeScreen(id, followNotUnFollow)*/
