package grand.app.moon.presentation.terms

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import com.structure.base_mvvm.databinding.FragmentTermsBinding
import grand.app.moon.presentation.terms.viewModels.TermsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>() {

  private val viewModel: TermsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_terms

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.terms)
    binding.includedToolbar.backIv.show()
    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen()}
  }

  override fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel.settingsResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.settingsData = it.value.data
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(
              it,
              retryAction = { viewModel.about() })
          }
        }
      }
    }

  }
}