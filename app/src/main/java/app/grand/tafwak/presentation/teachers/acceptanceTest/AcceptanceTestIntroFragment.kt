package app.grand.tafwak.presentation.teachers.acceptanceTest

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentAcceptanceTestIntroBinding
import app.grand.tafwak.presentation.teachers.acceptanceTest.viewModels.AcceptanceTestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcceptanceTestIntroFragment : BaseFragment<FragmentAcceptanceTestIntroBinding>() {

  private val viewModel: AcceptanceTestViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_acceptance_test_intro

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
}