package ma.ya.cometchatintegration.helperClasses

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class MADialogFragment<VDB : ViewDataBinding> : DialogFragment() {

	open val widthIsMatchParent: Boolean = true
	open val heightIsMatchParent: Boolean = false
	open val windowGravity: Int = Gravity.CENTER
	open val canceledOnTouchOutside: Boolean = true

	private var _binding: VDB? = null
	
	protected val binding get() = _binding!!

	@LayoutRes
	abstract fun getLayoutId(): Int
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
		initializeBindingVariables()
		binding.lifecycleOwner = viewLifecycleOwner
		
		return binding.root
	}
	
	protected open fun initializeBindingVariables() {}
	
	final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return customOnCreateDialog(savedInstanceState).also { dialog ->
			dialog.window?.apply {
				setLayout(
					if (widthIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
					if (heightIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
				)
				
				setGravity(windowGravity)
				
				onCreateDialogWindowChanges(this)
			}
			
			dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
			
			dialog.setOnKeyListener { _, keyCode, _ ->
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					onBackPressed()
					
					true
				}else {
					false
				}
			}
		}
	}
	
	override fun onResume() {
		super.onResume()
		
		dialog?.window?.apply {
			setLayout(
				if (widthIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
				if (heightIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
			)
			
			setGravity(windowGravity)
		}
	}
	
	protected open fun customOnCreateDialog(savedInstanceState: Bundle?): Dialog {
		return super.onCreateDialog(savedInstanceState)
	}

	@CallSuper
	override fun onCancel(dialog: DialogInterface) {
		onEitherCancelOrDismiss()
	}

	@CallSuper
	override fun onDismiss(dialog: DialogInterface) {
		onEitherCancelOrDismiss()

		super.onDismiss(dialog)
	}

	open fun onEitherCancelOrDismiss() {}

	/** calls [dismiss] if [canceledOnTouchOutside] is `true` otherwise does nothing isa. */
	open fun onBackPressed() {
		if (canceledOnTouchOutside) {
			dismiss()
		}
	}

	open fun onCreateDialogWindowChanges(window: Window) {}

}

/*
    public void launchActivityResult(int request, int resultCode, Intent result) {
    }
    protected void handleActions(Mutable mutable) {
        ((ParentActivity) requireContext()).handleActions(mutable);
    }
    public void toastError(String message) {
        ((ParentActivity) requireContext()).toastError(message);
    }
    public void deliverResultFromDialog(Mutable mutable) {
        if (getParentFragment() instanceof BaseFragment) {
            ((BaseFragment) getParentFragment()).handleMADialogFragmentResult(mutable);
        }

        for (var fragment : getParentFragmentManager().getFragments()) {
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).handleMADialogFragmentResult(mutable);
                break;
            }
        }
    }
}
 */
