package grand.app.moon.extensions

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.core.extenstions.showError
import grand.app.moon.databinding.DialogCustomYesNoWarningBinding
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

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveText: CharSequence,
    negativeText: CharSequence,
    isCancellable: Boolean = true,
    onPositiveButtonClick: (DialogInterface) -> Unit,
    onNegativeButtonClick: (DialogInterface) -> Unit,
    onDismissListener: () -> Unit = {},
) {
    kotlin.runCatching {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancellable)
            .setPositiveButton(positiveText) { dialog, _ ->
                onPositiveButtonClick(dialog)
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                onNegativeButtonClick(dialog)
            }
            .setOnDismissListener {
                onDismissListener()
            }
            .create()
            .show()
    }
}

fun <I> ActivityResultLauncher<I>.launchSafely(context: Context?, input: I) {
    try {
        launch(input)
    } catch (exception: ActivityNotFoundException) {
        context?.showError(context.getString(R.string.no_app_can_handle_this_action))
    }
}

fun Fragment.showCustomYesNoWarningDialog(
	title: String,
	description: String,
	positiveText: String = context?.getString(R.string.yes_confirm).orEmpty(),
	negativeText: String = context?.getString(R.string.no_thanks).orEmpty(),
	negativeClick: DialogCustomYesNoWarningBinding.(Dialog) -> Unit = { it.dismiss() },
	positiveClick: DialogCustomYesNoWarningBinding.(Dialog) -> Unit,
) = showCustomDialog<DialogCustomYesNoWarningBinding>(
	activity,
	R.layout.dialog_custom_yes_no_warning
) { dialog ->
	titleTextView.text = title
	msgTextView.text = description

	negativeButton.text = negativeText
	positiveButton.text = positiveText

	negativeButton.setOnClickListener {
		this.negativeClick(dialog)
	}
	positiveButton.setOnClickListener {
		this.positiveClick(dialog)
	}
}

fun <VDB : ViewDataBinding> showCustomDialog(
	activity: Activity?,
	@LayoutRes layoutRes: Int,
	getBackground: () -> Drawable = {
		activity?.let {
			InsetDrawable(
				ContextCompat.getDrawable(activity, R.drawable.dr_round_white_5) ?: ColorDrawable(Color.WHITE),
				activity.dpToPx(5f).roundToInt()
			)
		} ?: ColorDrawable(Color.WHITE)
	},
	widthIsMatchParent: Boolean = true,
	heightIsMatchParent: Boolean = false,
	cancelable: Boolean = true,
	canceledOnTouchOutside: Boolean = true,
	setups: VDB.(Dialog) -> Unit,
) {
	if (activity == null || activity.isFinishing) {
		return
	}

	val background = getBackground()

	val dialog = Dialog(activity)
	if (dialog.window != null) {
		dialog.window?.setBackgroundDrawable(background)
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
}
