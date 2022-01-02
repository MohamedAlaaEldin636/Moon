package com.structure.base_mvvm.presentation.teachers.compensationSessions

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentTeacherCompensationSessionsBinding
import com.structure.base_mvvm.presentation.teachers.compensationSessions.viewModels.TeacherCompensationSessionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherCompensationSessionsFragment :
  BaseFragment<FragmentTeacherCompensationSessionsBinding>() {

  private val viewModel: TeacherCompensationSessionsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teacher_compensation_sessions

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