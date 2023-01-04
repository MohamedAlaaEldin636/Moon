package grand.app.moon.extensions.bindingAdapter

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import grand.app.moon.extensions.orZero

@BindingAdapter("textView_setTextColorResBA")
fun TextView.setTextColorResBA(@ColorRes res: Int?) {
	setTextColor(ContextCompat.getColor(context, res.orZero()))
}
