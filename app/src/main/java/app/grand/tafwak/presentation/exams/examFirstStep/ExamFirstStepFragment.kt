package app.grand.tafwak.presentation.exams.examFirstStep

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentExamFirstStepBinding
import app.grand.tafwak.presentation.exams.examFirstStep.viewModels.ExamFirstStepViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamFirstStepFragment : BaseFragment<FragmentExamFirstStepBinding>() {

  private val viewModel: ExamFirstStepViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_exam_first_step

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