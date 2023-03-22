package ma.ya.cometchatintegration.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import ma.ya.cometchatintegration.R
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.math.roundToInt

fun FragmentActivity.showAlertDialog(
	title: String,
	message: String,
	onDismissListener: () -> Unit = {},
	onPositiveButtonClick: () -> Unit
) {
	AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(
			getString(R.string.ok)
		) { _, _ ->
			onPositiveButtonClick()
		}
		.setOnDismissListener {
			onDismissListener()
		}
		.create()
		.show()
}

fun <VDB : ViewDataBinding> showCustomDialog(
	activity: Activity?,
	@LayoutRes layoutRes: Int,
	getBackground: () -> Drawable = {
		activity?.let {
			InsetDrawable(
				ContextCompat.getDrawable(activity, R.drawable.dr_rect_5) ?: ColorDrawable(Color.WHITE),
				activity.dpToPx(16f).roundToInt()
			)
		} ?: ColorDrawable(Color.WHITE)
	},
	widthIsMatchParent: Boolean = true,
	heightIsMatchParent: Boolean = false,
	cancelable: Boolean = true,
	canceledOnTouchOutside: Boolean = true,
	gravity: Int = Gravity.CENTER,
	setups: VDB.(Dialog) -> Unit,
): ShownCustomDialog<VDB>? {
	if (activity == null || activity.isFinishing) {
		return null
	}

	val background = getBackground()

	val dialog = Dialog(activity)
	if (dialog.window != null) {
		dialog.window?.setBackgroundDrawable(background)
		dialog.window?.setGravity(gravity)
	}
	val binding = DataBindingUtil.inflate<VDB>(
		activity.layoutInflater, layoutRes, null, false
	)
	binding.setups(dialog)

	dialog.setContentView(binding.root)

	dialog.window?.setBackgroundDrawable(background)
	dialog.window?.setLayout(
		if (widthIsMatchParent) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT,
		if (heightIsMatchParent) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
	)

	dialog.setCancelable(cancelable)
	dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
	dialog.show()

	return ShownCustomDialog(binding, dialog)
}

data class ShownCustomDialog<VDB : ViewDataBinding>(val binding: VDB, val dialog: Dialog)
