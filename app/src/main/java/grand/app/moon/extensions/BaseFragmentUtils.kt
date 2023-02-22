package grand.app.moon.extensions

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.DialogRetryErrorHandlingBinding
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.FailureStatus
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.utils.showLoadingDialog
import grand.app.moon.presentation.splash.MABaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * - Can't be dismissed, If wanna show a can be dismissed error use [Toast] or [Snackbar] isa.
 */
fun Activity.showRetryErrorDialogWithBackNegativeButton(
	msg: String = getString(R.string.something_went_wrong_please_try_again),
	negativeButton: String = getString(R.string.back),
	negativeButtonAction: () -> Unit = {
		onBackPressed()
	},
	onRetry: () -> Unit
) = showRetryErrorDialog(this, msg, negativeButton, negativeButtonAction, onRetry)

/**
 * - Can't be dismissed, If wanna show a can be dismissed error use [Toast] or [Snackbar] isa.
 */
fun Activity.showRetryErrorDialogWithCancelNegativeButton(
	msg: String = getString(R.string.something_went_wrong_please_try_again),
	negativeButton: String = getString(R.string.cancel),
	negativeButtonAction: () -> Unit = {},
	onRetry: () -> Unit
) = showRetryErrorDialog(this, msg, negativeButton, negativeButtonAction, onRetry)

/**
 * - Can't be dismissed, If wanna show a can be dismissed error use [Toast] or [Snackbar] isa.
 */
fun BaseFragment<*>.showRetryErrorDialogWithBackNegativeButton(
	msg: String = getString(R.string.something_went_wrong_please_try_again),
	negativeButton: String = getString(R.string.back),
	negativeButtonAction: () -> Unit = {
		if (findNavController().graph.id == R.id.nav_home && findNavController().graph.startDestinationId == findNavController().currentDestination?.id) {
			hideLoading()

			activity?.onBackPressed()
		}else {
			hideLoading()

			findNavController().navigateUp()
		}
	},
	onRetry: () -> Unit
) = showRetryErrorDialog(activity, msg, negativeButton, negativeButtonAction, onRetry)

/**
 * - Can't be dismissed, If wanna show a can be dismissed error use [Toast] or [Snackbar] isa.
 */
fun BaseFragment<*>.showRetryErrorDialogWithCancelNegativeButton(
	msg: String = getString(R.string.something_went_wrong_please_try_again),
	negativeButton: String = getString(R.string.cancel),
	negativeButtonAction: () -> Unit = {},
	onRetry: () -> Unit
) = showRetryErrorDialog(activity, msg, negativeButton, negativeButtonAction, onRetry)

fun MADialogFragment<*>.showRetryErrorDialogWithCancelNegativeButton(
	msg: String = getString(R.string.something_went_wrong_please_try_again),
	negativeButton: String = getString(R.string.cancel),
	negativeButtonAction: () -> Unit = {},
	onRetry: () -> Unit
) = showRetryErrorDialog(activity, msg, negativeButton, negativeButtonAction, onRetry)

private fun showRetryErrorDialog(
	activity: Activity?,
	msg: String,
	negativeButton: String,
	negativeButtonAction: () -> Unit,
	onRetry: () -> Unit
) {
	if (activity == null || activity.isFinishing) {
		return
	}

	val progressDialog = Dialog(activity)
	if (progressDialog.window != null) {
		progressDialog.window?.setBackgroundDrawable(
			ColorDrawable(Color.GREEN)
		)
	}
	val binding = DataBindingUtil.inflate<DialogRetryErrorHandlingBinding>(
		activity.layoutInflater, R.layout.dialog_retry_error_handling, null, false
	)
	binding.msgTextView.text = msg
	binding.negativeButton.text = negativeButton
	binding.negativeButton.setOnClickListener {
		progressDialog.dismissSafely()
		negativeButtonAction()
	}
	binding.positiveButton.setOnClickListener {
		progressDialog.dismissSafely()
		onRetry()
	}

	progressDialog.setContentView(binding.root)

	progressDialog.window?.setBackgroundDrawable(
		InsetDrawable(
			ContextCompat.getDrawable(activity, R.drawable.dr_round_white_16) ?: ColorDrawable(Color.WHITE),
			activity.dpToPx(16f).roundToInt()
		)
	)
	progressDialog.window?.setLayout(
		ViewGroup.LayoutParams.MATCH_PARENT,
		ViewGroup.LayoutParams.WRAP_CONTENT
	)

	progressDialog.setCancelable(false)
	progressDialog.setCanceledOnTouchOutside(false)
	progressDialog.showSafely()
}

fun <T> MABaseActivity<*>.handleRetryAbleActionCancellable(
	action: suspend () -> Resource<BaseResponse<T?>>,
	onSuccess: (T) -> Unit
) {
	handleRetryAbleAction(
		showLoading = { showLoading() },
		hideLoading = { hideLoading() },
		lifecycleScope,
		action,
		onError = {
			showRetryErrorDialogWithCancelNegativeButton(
				it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
			) {
				handleRetryAbleActionCancellable(action, onSuccess)
			}
		},
		onSuccess
	)
}

fun <T> BaseActivity<*>.handleRetryAbleActionCancellable(
	action: suspend () -> Resource<BaseResponse<T?>>,
	onSuccess: (T) -> Unit
) {
	handleRetryAbleAction(
		showLoading = { showLoading() },
		hideLoading = { hideLoading() },
		lifecycleScope,
		action,
		onError = {
			showRetryErrorDialogWithCancelNegativeButton(
				it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
			) {
				handleRetryAbleActionCancellable(action, onSuccess)
			}
		},
		onSuccess
	)
}

fun <T> BaseFragment<*>.handleRetryAbleActionCancellable(
	action: suspend () -> Resource<BaseResponse<T?>>,
	showLoadingAction: () -> Unit = { showLoading() },
	hideLoadingAction: () -> Unit = { hideLoading() },
	onSuccess: (T) -> Unit
) {
	handleRetryAbleAction(
		showLoading = { showLoadingAction() },
		hideLoading = { hideLoadingAction() },
		lifecycleScope,
		action,
		onError = {
			showRetryErrorDialogWithCancelNegativeButton(
				it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
			) {
				handleRetryAbleActionCancellable(action, showLoadingAction, hideLoadingAction, onSuccess)
			}
		},
		onSuccess
	)
}

fun <T> BaseFragment<*>.handleRetryAbleActionCancellableNullable(
	action: suspend () -> Resource<BaseResponse<T?>>,
	onSuccess: (T?) -> Unit
) {
	handleRetryAbleActionNullable(
		showLoading = { showLoading() },
		hideLoading = { hideLoading() },
		lifecycleScope,
		action,
		onError = {
			showRetryErrorDialogWithCancelNegativeButton(
				it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
			) {
				handleRetryAbleActionCancellableNullable(action, onSuccess)
			}
		},
		onSuccess
	)
}

fun <T> MADialogFragment<*>.handleRetryAbleActionCancellableNullable(
	action: suspend () -> Resource<BaseResponse<T?>>,
	onSuccess: (T?) -> Unit
) {
	handleRetryAbleActionNullable(
		showLoading = { showLoading() },
		hideLoading = { hideLoading() },
		lifecycleScope,
		action,
		onError = {
			showRetryErrorDialogWithCancelNegativeButton(
				it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
			) {
				handleRetryAbleActionCancellableNullable(action, onSuccess)
			}
		},
		onSuccess
	)
}

fun <T> BaseFragment<*>.handleRetryAbleActionOrGoBack(
	scope: CoroutineScope = lifecycleScope,
	showLoadingCode: () -> Unit = { showLoading() },
	hideLoadingCode: () -> Unit = { hideLoading() },
	onErrorCode: ((failure: Resource.Failure, selfBlock: () -> Unit) -> Unit)? = null,
	action: suspend () -> Resource<BaseResponse<T?>>,
	onSuccess: (T) -> Unit
) {
	handleRetryAbleAction(
		showLoading = { showLoadingCode() },
		hideLoading = { hideLoadingCode() },
		scope,
		action,
		onError = {
			if (onErrorCode != null) {
				onErrorCode(it) {
					handleRetryAbleActionOrGoBack(scope, showLoadingCode, hideLoadingCode, onErrorCode, action, onSuccess)
				}
			}else {
				showRetryErrorDialogWithBackNegativeButton(
					it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
				) {
					handleRetryAbleActionOrGoBack(scope, showLoadingCode, hideLoadingCode, null, action, onSuccess)
				}
			}
		},
		onSuccess
	)
}

fun <T> BaseFragment<*>.handleRetryAbleActionOrGoBackNullable(
	action: suspend () -> Resource<BaseResponse<T?>>,
	onSuccess: (T?) -> Unit
) {
	handleRetryAbleActionNullable(
		showLoading = { showLoading() },
		hideLoading = { hideLoading() },
		lifecycleScope,
		action,
		onError = {
			showRetryErrorDialogWithBackNegativeButton(
				it.message.orElseIfNullOrEmpty(getString(R.string.something_went_wrong_please_try_again))
			) {
				handleRetryAbleActionOrGoBackNullable(action, onSuccess)
			}
		},
		onSuccess
	)
}

private fun <T> handleRetryAbleAction(
	showLoading: () -> Unit,
	hideLoading: () -> Unit,
	scope: CoroutineScope,
	action: suspend () -> Resource<BaseResponse<T?>>,
	onError: (Resource.Failure) -> Unit,
	onSuccess: (T) -> Unit,
) {
	scope.launch {
		showLoading()
		val value = action()
		hideLoading()
		when (value) {
			is Resource.Failure -> onError(value)
			is Resource.Success -> {
				val data = value.value.data

				if (data == null) {
					onError(Resource.Failure(FailureStatus.OTHER))
				}else {
					onSuccess(data)
				}
			}
			else -> { /* shouldn't happen */ }
		}
	}
}

private fun <T> handleRetryAbleActionNullable(
	showLoading: () -> Unit,
	hideLoading: () -> Unit,
	scope: CoroutineScope,
	action: suspend () -> Resource<BaseResponse<T?>>,
	onError: (Resource.Failure) -> Unit,
	onSuccess: (T?) -> Unit,
) {
	scope.launch {
		showLoading()
		val value = action()
		hideLoading()
		when (value) {
			is Resource.Failure -> onError(value)
			is Resource.Success -> onSuccess(value.value.data)
			else -> { /* shouldn't happen */ }
		}
	}
}
