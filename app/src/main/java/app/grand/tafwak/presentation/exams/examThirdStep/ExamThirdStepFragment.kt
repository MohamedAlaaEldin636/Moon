package app.grand.tafwak.presentation.exams.examThirdStep

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentExamThirdStepBinding
import app.grand.tafwak.presentation.exams.examThirdStep.viewModels.ExamThirdStepViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamThirdStepFragment : BaseFragment<FragmentExamThirdStepBinding>() {

  private val viewModel: ExamThirdStepViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_exam_third_step

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