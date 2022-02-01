package app.grand.tafwak.presentation.exams

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentExamsBinding
import app.grand.tafwak.presentation.exams.viewModels.ExamsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamsFragment : BaseFragment<FragmentExamsBinding>() {

  private val viewModel: ExamsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_exams

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.privacy)
//    binding.includedToolbar.backIv.show()
//    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen()}
  }
}