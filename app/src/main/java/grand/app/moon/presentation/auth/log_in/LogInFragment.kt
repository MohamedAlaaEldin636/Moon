package grand.app.moon.presentation.auth.log_in

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import grand.app.moon.domain.base.FieldsValidation
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.getDeviceId
import grand.app.moon.presentation.base.utils.showNoApiErrorAlert
import grand.app.moon.databinding.FragmentLogInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener


@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>() {

  private val viewModel: LogInViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_log_in

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setupObservers() {
    initView()
    viewModel.clickEvent.observe(this) {
      when (it) {
        Constants.BACK -> activity?.finish()
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel._logInResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openConfirm()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }

        }
      }
    }
  }

  private  val TAG = "LogInFragment"
  private fun initView() {
    viewModel.request.country_code = "+${binding.ccp.selectedCountryCode}"
    binding.ccp.setOnCountryChangeListener(OnCountryChangeListener {
      viewModel.request.country_code = "+${binding.ccp.selectedCountryCode}"
    })
    binding.edtLoginPhone.addTextChangedListener(object : TextWatcher {

      override fun afterTextChanged(s: Editable) {}

      override fun beforeTextChanged(s: CharSequence, start: Int,
                                     count: Int, after: Int) {
        binding.btnPhone.isEnabled = count == 0
      }

      override fun onTextChanged(s: CharSequence, start: Int,
                                 before: Int, count: Int) {
      }
    })
  }

  private fun openConfirm() {
    navigateSafe(
      LogInFragmentDirections.actionLogInFragmentToFragmentConfirmCode(
        viewModel.request.phone,Constants.Verify)
    )
  }

  val activityResultGoogleSignIn = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {

    Log.d(TAG, ": activityResultGoogleSignIn")

    if (it.resultCode == Activity.RESULT_OK) {
      Log.d(TAG, ": RESULT_OK")
      // The Task returned from this call is always completed, no need to attach
      // a listener.
      val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

      try {
        val account = task.getResult(ApiException::class.java)
        Log.d(TAG, ": account")

        val id = account.id!!


        viewModel.performSocialLoginWithApi(this, id)
      }catch (throwable: Throwable) {

        viewModel.showError(requireContext(),getString(R.string.something_went_wrong_please_try_again))
      }
    }
  }

}