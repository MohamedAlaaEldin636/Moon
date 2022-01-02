package com.structure.base_mvvm.presentation.teachers.studentProfile

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentGroupStudentProfileBinding
import com.structure.base_mvvm.presentation.databinding.FragmentSendReportBinding
import com.structure.base_mvvm.presentation.teachers.report.viewModels.SendReportViewModel
import com.structure.base_mvvm.presentation.teachers.studentProfile.viewModels.GroupStudentProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupStudentProfileFragment : BaseFragment<FragmentGroupStudentProfileBinding>() {

  private val viewModel: GroupStudentProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_group_student_profile

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