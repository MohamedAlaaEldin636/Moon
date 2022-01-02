package com.structure.base_mvvm.presentation.teachers.my_groups

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentMyGroupsBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTeacherGroupsBinding
import com.structure.base_mvvm.presentation.teachers.my_groups.viewModels.TeacherGroupsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherGroupsFragment : BaseFragment<FragmentTeacherGroupsBinding>() {

  private val viewModel: TeacherGroupsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teacher_groups

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

  override fun setupObservers() {
//    viewModel.clickEvent.observeForever {
//      if (it == Constants.REVIEWS)
//
//    }
  }
}