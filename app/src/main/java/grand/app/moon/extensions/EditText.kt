package grand.app.moon.extensions

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("editText_setOnEditorActionListenerBA")
fun EditText.setOnEditorActionListenerBA(listener: TextView.OnEditorActionListener?) {
	if (listener != null) {
		setOnEditorActionListener(listener)
	}
}
