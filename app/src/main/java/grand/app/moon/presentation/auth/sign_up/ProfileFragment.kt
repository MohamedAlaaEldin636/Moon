package grand.app.moon.presentation.auth.sign_up

import android.util.Log
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentProfileBinding
import grand.app.moon.presentation.base.BaseFragment

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

  private val viewModel: ProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_profile

  override
  fun setBindingVariables() {
//    binding.viewModel = viewModel
//    viewModel.request.device_token = getDeviceId(requireContext())
//    if(viewModel.request.isLogin){
//      binding.imgLoginLogo.loadCircleImage(viewModel.userUseCase.accountRepository.getUserLocal().image,null)
//    }
  }

  private val TAG = "SignUpFragment"

  override
  fun setupObservers() {
//    viewModel.clickEvent.observe(this) {
//      Log.d(TAG, "event $it")
//      when (it) {
//        Constants.LOGIN -> backToPreviousScreen()
//        Constants.PICKER_IMAGE -> {
//          singleTedBottomPicker(requireActivity())
//        }
//      }
//    }
//    selectedImages.observeForever {
//      selectedImages.value?.path?.let { it1 ->
//        run {
//          viewModel.request.setImage(it1,Constants.IMAGE)
//          viewModel.notifyChange()
//        }
//      }
//    }
//    lifecycleScope.launchWhenResumed {
//      viewModel.response.collect {
//        handleLoading(it)
//        if (it is Resource.Success) {
//          showMessageSuccess(it.value.message)
//          navigateSafe(SignUpFragmentDirections.actionOpenConfirmCodeFragment(viewModel.request.email,Constants.VERIFY,Constants.LOGIN))
//        }
//      }
//    }
  }

}