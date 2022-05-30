package grand.app.moon.presentation.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.showSystemAppDetailsPage
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.onesignal.OneSignal
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.extensions.checkGalleryPermissions
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.base.utils.hideLoadingDialog
import grand.app.moon.presentation.base.utils.showLoadingDialog
import grand.app.moon.presentation.home.HomeActivity
import gun0912.tedbottompicker.TedRxBottomPicker
import java.util.Locale

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

  private var _binding: VB? = null
  open val binding get() = _binding!!
  private var mRootView: View? = null
  private var hasInitializedRootView = false
  private var progressDialog: Dialog? = null
  val selectedImages = SingleLiveEvent<Uri>()

  override
  fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    if (mRootView == null) {
      initViewBinding(inflater, container)
    }

    return mRootView
  }


  private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
    _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

    mRootView = binding.root
    binding.lifecycleOwner = this
    binding.executePendingBindings()
  }

  /*
  01010998759
  1016171926
   */


  override
  fun onResume() {
    super.onResume()

    registerListeners()
  }

  override
  fun onPause() {
    unRegisterListeners()

    super.onPause()
  }

  override
  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (!hasInitializedRootView) {
      getFragmentArguments()
      setBindingVariables()
      setUpViews()
      observeAPICall()
      setupObservers()

      hasInitializedRootView = true
    }
  }

  @LayoutRes
  abstract fun getLayoutId(): Int

  open fun registerListeners() {}

  open fun unRegisterListeners() {}

  open fun getFragmentArguments() {}

  open fun setBindingVariables() {}

  open fun setUpViews() {}

  open fun observeAPICall() {}

  open fun setupObservers() {}

  fun showLoading() {
    hideLoading()
    progressDialog = showLoadingDialog(requireActivity(), null)
  }

  fun showLoading(hint: String?) {
    hideLoading()
    progressDialog = showLoadingDialog(requireActivity(), hint)
  }

  fun hideLoading() = hideLoadingDialog(progressDialog, requireActivity())

  fun setLanguage(language: String) {
    (requireActivity() as BaseActivity<*>).updateLocale(language)
  }

  val currentLanguage: Locale
    get() = Locale.getDefault()

  // check Permissions
  private fun checkGalleryPermissions(fragmentActivity: FragmentActivity): Boolean {
    return fragmentActivity.checkGalleryPermissions()

  }

  fun openLoginActivity(){
    startActivity(Intent(activity, AuthActivity::class.java))
  }

  // Pick Single image
  @SuppressLint("CheckResult")
  fun singleTedBottomPicker(fragmentActivity: FragmentActivity) {
    if (checkGalleryPermissions(fragmentActivity)) {
      TedRxBottomPicker.with(fragmentActivity)
        .show()
        .subscribe({
          selectedImages.value = it
        }, Throwable::printStackTrace)
    }
  }

  @SuppressLint("CheckResult")
  fun multiTedBottomPicker(fragmentActivity: FragmentActivity) {
    if (checkGalleryPermissions(fragmentActivity)) {
      TedRxBottomPicker.with(fragmentActivity)
        .show()
        .subscribe({
          selectedImages.value = it
        }, Throwable::printStackTrace)
    }
  }

}