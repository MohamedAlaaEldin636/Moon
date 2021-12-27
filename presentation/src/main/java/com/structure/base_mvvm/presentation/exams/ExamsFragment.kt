package com.structure.base_mvvm.presentation.exams

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentExamsBinding
import com.structure.base_mvvm.presentation.exams.viewModels.ExamsViewModel
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