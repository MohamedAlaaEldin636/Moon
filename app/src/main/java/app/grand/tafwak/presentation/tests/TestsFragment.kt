package app.grand.tafwak.presentation.tests

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentTestsBinding
import app.grand.tafwak.presentation.tests.viewModels.TestsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestsFragment : BaseFragment<FragmentTestsBinding>() {

  private val viewModel: TestsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_tests

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