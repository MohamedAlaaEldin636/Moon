package grand.app.moon.extensions

import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import grand.app.moon.core.MyApplication

val AndroidViewModel.app: MyApplication
	get() = getApplication()

fun AndroidViewModel.getString(@StringRes res: Int) = app.getString(res)
