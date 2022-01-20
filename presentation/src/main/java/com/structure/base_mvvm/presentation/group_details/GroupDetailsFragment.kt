package com.structure.base_mvvm.presentation.group_details

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentGroupDetailsBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTeacherProfileBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.group_details.viewModels.GroupDetailsViewModel
import com.structure.base_mvvm.presentation.home.HomeFragmentDirections
import com.structure.base_mvvm.presentation.teachers.viewModels.TeacherProfileViewModel
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailsFragment : BaseFragment<FragmentGroupDetailsBinding>() {

  private val viewModel: GroupDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_group_details

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