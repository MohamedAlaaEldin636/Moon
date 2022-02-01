package app.grand.tafwak.presentation.compensationSessions

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.compensationSessions.viewModels.CompensationSessionsViewModel
import com.structure.base_mvvm.databinding.FragmentCompensationSessionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompensationSessionsFragment : BaseFragment<FragmentCompensationSessionsBinding>() {

  private val viewModel: CompensationSessionsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_compensation_sessions

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.settings)
//    binding.includedToolbar.backIv.hide()
  }

  override fun setupObservers() {
//    viewModel.clickEvent.observeForever {
//      if (it == Constants.REVIEWS)
//
//    }
  }
}