package com.structure.base_mvvm.presentation.exams.examResult

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentExamResultBinding
import com.structure.base_mvvm.presentation.exams.examResult.viewModels.ExamResultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamResultFragment : BaseFragment<FragmentExamResultBinding>() {

  private val viewModel: ExamResultViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_exam_result

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