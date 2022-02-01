package app.grand.tafwak.presentation.teachers.report

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentSendReportBinding
import app.grand.tafwak.presentation.teachers.report.viewModels.SendReportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendReportFragment : BaseFragment<FragmentSendReportBinding>() {

  private val viewModel: SendReportViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_send_report

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