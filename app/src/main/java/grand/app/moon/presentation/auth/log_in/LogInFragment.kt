package grand.app.moon.presentation.auth.log_in

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.databinding.FragmentLogInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import grand.app.moon.core.extenstions.showErrorToast
import grand.app.moon.helpers.login.SocialRequest


@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>() {
  private val viewModel: LogInViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_log_in

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    arguments?.let {
      if (it.containsKey("social_id") && it.getString("social_id") != null)
        viewModel.showSocial.set(false)
    }
  }

  override
  fun setupObservers() {
    initView()
    viewModel.clickEvent.observe(this) {
      when (it) {
        Constants.BACK -> activity?.finish()
        Constants.GOOGLE -> {
          activityResultGoogleSignIn.launch(viewModel.googleClient.signInIntent)
        }
        Constants.FACEBOOK -> activity?.finish()
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
            when (viewModel.typeRequest) {
              Constants.SOCIAL_TYPE -> when (viewModel.checkUser()) {
                true -> makeIntegrationWithRedirectHome(viewModel.userLocalUseCase.invoke().id)
                else -> findNavController().navigate(
                  LogInFragmentDirections.actionLogInFragmentSelf(
                    viewModel.socialRequest.provider_id
                  )
                )
              }
              else -> openConfirm()
            }
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }

        }
      }
    }
  }

  private val TAG = "LogInFragment"
  private fun initView() {
    viewModel.request.country_code = "+${binding.ccp.selectedCountryCode}"
    binding.ccp.setOnCountryChangeListener(OnCountryChangeListener {
      viewModel.request.country_code = "+${binding.ccp.selectedCountryCode}"
    })
    binding.edtLoginPhone.addTextChangedListener(object : TextWatcher {

      override fun afterTextChanged(s: Editable) {}

      override fun beforeTextChanged(
        s: CharSequence, start: Int,
        count: Int, after: Int
      ) {
        binding.btnPhone.isEnabled = count == 0
      }

      override fun onTextChanged(
        s: CharSequence, start: Int,
        before: Int, count: Int
      ) {
      }
    })
  }

  private fun openConfirm() {
//    navigateSafe(
//      LogInFragmentDirections.actionLogInFragmentToFragmentConfirmCode(
//        viewModel.request.country_code,viewModel.request.phone, Constants.Verify
//      )
//    )
  }


  var activityResultGoogleSignIn = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    Log.d(TAG, "${it.resultCode}: , ${Activity.RESULT_OK}")

    if (it.resultCode == Activity.RESULT_OK) {
      // The Task returned from this call is always completed, no need to attach
      // a listener.
      val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

      try {
        val account = task.getResult(ApiException::class.java)

        val request = SocialRequest(
          provider_id = account.id!!,
          objective = "google"
        )
        account.displayName?.let {
          request.name = it
        }
        account.email?.let {
          request.email = it
        }
        viewModel.setSocialRegister(
          request
        )
      } catch (throwable: Throwable) {
        Log.d(TAG, "${throwable.message}: ")
        requireContext().showErrorToast(getString(R.string.something_went_wrong_please_try_again))
      }
    }
  }


}