package grand.app.moon.presentation.auth.confirmCode

import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.databinding.FragmentConfirmCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.presentation.home.HomeActivity
import kotlinx.coroutines.flow.collect
import com.onesignal.OneSignal
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.base.extensions.*


@AndroidEntryPoint
class ConfirmCodeFragment : BaseFragment<FragmentConfirmCodeBinding>() {

  private val viewModel: ConfirmViewModel by viewModels()

  lateinit var smsBroadcastReceiver: SMSBroadcastReceiver
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    smsBroadcastReceiver = SMSBroadcastReceiver()
//
//    val otpListener = object : SMSBroadcastReceiver.OTPListener {
//      override fun onOTPReceived(otp: String) {
////        Toast.makeText(this@MainActivity, otp , Toast.LENGTH_LONG).show()
//      }
//
//      override fun onOTPTimeOut() {
////        Toast.makeText(this@MainActivity,"TimeOut", Toast.LENGTH_SHORT).show()
//      }
//    }

//    smsBroadcastReceiver.injectOTPListener(otpListener)
//    requireActivity().registerReceiver(smsBroadcastReceiver,
//      IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
//    )

//    startSMSListener()

  }
  private val TAG = "ConfirmCodeFragment"
  private fun startSMSListener() {
    Log.d("CodeVerification", "startSMSListener()")

    val client = SmsRetriever.getClient(requireActivity())
    val retriever = client.startSmsRetriever()
//    retriever.addOnSuccessListener {
//      val listener = object : SMSBroadcastReceiver.OTPListener {
//        override fun onOTPReceived(otp: String) {
//          TODO("Not yet implemented")
//          Log.d(TAG, "onOTPReceived: $otp")
//        }
//
//        override fun onOTPTimeOut() {
//          TODO("Not yet implemented")
//          Log.d(TAG, "onOTPTimeOut: TIMEOUT")
//        }
//      }
//      smsBroadcastReceiver.injectOTPListener(listener)
//      requireActivity().registerReceiver(smsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
//    }
    retriever.addOnSuccessListener {
//      showMessage("addOnSuccessListener")
      Log.d(TAG, "startSMSListener: ${retriever.result}")
    }

    retriever.addOnFailureListener {
//      showMessage("addOnFailureListener")
    }
  }

  override
  fun getLayoutId() = R.layout.fragment_confirm_code

  override
  fun setBindingVariables() {
    binding.viewmodel = viewModel
    if(arguments != null) {
      if(requireArguments().containsKey("country_code")) {
        val args: ConfirmCodeFragmentArgs by navArgs()
        viewModel.request.country_code = args.countryCode
        viewModel.request.phone = args.phone
        viewModel.request.type = args.type
      }else if(requireArguments().containsKey("profile")){
        val gson = GsonBuilder().create()
        viewModel.profileRequest = gson.fromJson<UpdateProfileRequest>(requireArguments().getString("profile"), object :
          TypeToken<UpdateProfileRequest>() {}.type)
        viewModel.request.country_code = viewModel.profileRequest.country_code
        viewModel.request.phone = viewModel.profileRequest.phone
      }
    }
  }

  override
  fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel.verifyResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            if(viewModel.profileRequest.country_code.isEmpty())//usual login
              makeIntegrationWithRedirectHome(viewModel.userLocalUseCase.invoke().id)
            else{
              //update profile
              viewModel.updateProfile()
            }
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel._sendCode.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.profileResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            makeIntegrationWithRedirectHome(viewModel.userLocalUseCase.invoke().id)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }

  }


  override fun onDestroy() {
    super.onDestroy()
    unRegisterListeners()
  }

  override fun onStart() {
    super.onStart()
  }

  override fun onDestroyView() {
    super.onDestroyView()
//    countDownTimer.cancel()
  }
}