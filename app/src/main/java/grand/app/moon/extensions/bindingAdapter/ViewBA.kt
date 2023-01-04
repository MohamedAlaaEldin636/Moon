package grand.app.moon.extensions.bindingAdapter

import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.orZero

@BindingAdapter("view_setBackgroundRes")
fun View.setBackgroundRes(@DrawableRes res: Int?) {
	setBackgroundResource(res.orZero())
}
