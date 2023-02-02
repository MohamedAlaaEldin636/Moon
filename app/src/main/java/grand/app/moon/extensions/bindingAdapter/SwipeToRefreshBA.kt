package grand.app.moon.extensions.bindingAdapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("swipeRefreshLayout_setOnRefreshListenerBA")
fun SwipeRefreshLayout.setOnRefreshListenerBA(listener: View.OnClickListener?) {
	if (listener != null) {
		setOnRefreshListener { listener.onClick(this) }
	}
}
