package com.structure.base_mvvm.presentation.exams.examSecondStep

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentExamSecondStepBinding
import com.structure.base_mvvm.presentation.exams.examSecondStep.viewModels.ExamSecondStepViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamSecondStepFragment : BaseFragment<FragmentExamSecondStepBinding>() {

  private val viewModel: ExamSecondStepViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_exam_second_step

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