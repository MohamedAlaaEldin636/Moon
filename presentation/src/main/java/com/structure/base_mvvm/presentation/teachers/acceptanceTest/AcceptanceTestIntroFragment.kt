package com.structure.base_mvvm.presentation.teachers.acceptanceTest

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentAcceptanceTestIntroBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.teachers.acceptanceTest.viewModels.AcceptanceTestViewModel
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcceptanceTestIntroFragment : BaseFragment<FragmentAcceptanceTestIntroBinding>() {

  private val viewModel: AcceptanceTestViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_acceptance_test_intro

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