package app.grand.tafwak.presentation.contactUs

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.auth.enums.AuthFieldsValidation
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.*
import app.grand.tafwak.presentation.base.utils.showNoApiErrorAlert
import app.grand.tafwak.presentation.base.utils.showSuccessAlert
import app.grand.tafwak.presentation.contactUs.viewModels.ContactUsViewModel
import com.structure.base_mvvm.databinding.FragmentContactUsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {

  private val viewModel: ContactUsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_contact_us

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.about)
//    binding.includedToolbar.backIv.show()
//    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen() }
  }

  override fun setupObservers() {

    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_NAME.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.register_name))
        }
        AuthFieldsValidation.EMPTY_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.empty_email))
        }
        AuthFieldsValidation.INVALID_EMAIL.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.invalid_email))
        }
        AuthFieldsValidation.EMPTY_PHONE.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.register_phone))
        }
        AuthFieldsValidation.EMPTY_CONTENT.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.message))
        }

      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.contactResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            showSuccessAlert(requireActivity(), it.value.message)
            backToPreviousScreen()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(
              it,
              retryAction = { viewModel.onContactClicked() })
          }

        }
      }
    }
  }
}