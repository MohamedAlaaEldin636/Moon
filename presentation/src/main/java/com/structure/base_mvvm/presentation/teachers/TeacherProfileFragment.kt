package com.structure.base_mvvm.presentation.teachers

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentTeacherProfileBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.home.HomeFragmentDirections
import com.structure.base_mvvm.presentation.teachers.viewModels.TeacherProfileViewModel
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherProfileFragment : BaseFragment<FragmentTeacherProfileBinding>() {

  private val viewModel: TeacherProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teacher_profile

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
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEWS)
        toReviews()
    }
  }

  fun toReviews() {
    navigateSafe(TeacherProfileFragmentDirections.actionToReviewsFragment())
  }
}