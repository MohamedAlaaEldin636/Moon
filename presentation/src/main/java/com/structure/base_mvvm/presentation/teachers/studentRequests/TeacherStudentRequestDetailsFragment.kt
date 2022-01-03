package com.structure.base_mvvm.presentation.teachers.studentRequests

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentStudentRequestDetailsBinding
import com.structure.base_mvvm.presentation.databinding.FragmentStudentsRequestsBinding
import com.structure.base_mvvm.presentation.teachers.studentRequests.viewModels.TeacherStudentRequestDetailsViewModel
import com.structure.base_mvvm.presentation.teachers.studentRequests.viewModels.TeacherStudentRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherStudentRequestDetailsFragment : BaseFragment<FragmentStudentRequestDetailsBinding>() {

  private val viewModel: TeacherStudentRequestDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_student_request_details

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