package com.structure.base_mvvm.presentation.teachers.studentRequests

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentStudentsRequestsBinding
import com.structure.base_mvvm.presentation.teachers.studentRequests.viewModels.TeacherStudentRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherStudentRequestsFragment : BaseFragment<FragmentStudentsRequestsBinding>() {

  private val viewModel: TeacherStudentRequestsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_students_requests

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

  override fun setupObservers() {
//    viewModel.clickEvent.observeForever {
//      if (it == Constants.REVIEWS)
//
//    }
  }
}