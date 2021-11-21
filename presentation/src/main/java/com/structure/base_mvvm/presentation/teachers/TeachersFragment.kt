package com.structure.base_mvvm.presentation.teachers

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeachersFragment : BaseFragment<FragmentTeachersBinding>() {

  private val viewModel: TeachersViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teachers

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