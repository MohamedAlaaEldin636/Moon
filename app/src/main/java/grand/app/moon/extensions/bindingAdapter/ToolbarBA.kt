package grand.app.moon.extensions.bindingAdapter

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

@BindingAdapter("toolbar_setNavIconClick")
fun Toolbar.setNavIconClick(listener: View.OnClickListener?) {
	setNavigationOnClickListener(listener)
}
