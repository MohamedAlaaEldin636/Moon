package com.structure.base_mvvm.presentation.tests

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTestsBinding
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
import com.structure.base_mvvm.presentation.tests.viewModels.TestsViewModel
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
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.search)
//    binding.includedToolbar.backIv.hide()
  }
}