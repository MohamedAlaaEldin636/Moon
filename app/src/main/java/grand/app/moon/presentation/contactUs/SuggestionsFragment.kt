package grand.app.moon.presentation.contactUs

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.showSuccessAlert
import grand.app.moon.presentation.contactUs.viewModels.ContactUsViewModel
import grand.app.moon.databinding.FragmentSuggestionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SuggestionsFragment : BaseFragment<FragmentSuggestionsBinding>() {

  private val viewModel: ContactUsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_suggestions

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setupObservers() {
    viewModel.validationException.observe(this) {
      when (it) {

      }
    }

//    lifecycleScope.launchWhenResumed {
//      viewModel.contactReasonResponse.collect {
//        when (it) {
//          Resource.Loading -> {
//            hideKeyboard()
//            showLoading()
//          }
//          is Resource.Success -> {
//            hideLoading()
//            showSuccessAlert(requireActivity(), it.value.message)
//            backToPreviousScreen()
//          }
//          is Resource.Failure -> {
//            hideLoading()
//            handleApiError(
//              it,
//              retryAction = { viewModel.onContactClicked() })
//          }
//
//        }
//      }
//    }
  }
}