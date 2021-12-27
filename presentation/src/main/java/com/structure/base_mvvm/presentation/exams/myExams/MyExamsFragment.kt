package com.structure.base_mvvm.presentation.exams.myExams

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentExamsBinding
import com.structure.base_mvvm.presentation.databinding.FragmentMyExamsBinding
import com.structure.base_mvvm.presentation.exams.myExams.viewModels.MyExamsViewModel
import com.structure.base_mvvm.presentation.exams.viewModels.ExamsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyExamsFragment : BaseFragment<FragmentMyExamsBinding>() {

  private val viewModel: MyExamsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_my_exams

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