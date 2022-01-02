package com.structure.base_mvvm.presentation.teachers.addGroup

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentAddGroupBinding
import com.structure.base_mvvm.presentation.teachers.addGroup.viewModels.AddGroupViewModel
import com.structure.base_mvvm.presentation.teachers.viewModels.AskPrivateSessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupFragment : BaseFragment<FragmentAddGroupBinding>() {

  private val viewModel: AddGroupViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_add_group

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
//    navigateSafe(TeacherProfileFragmentDirections.actionToReviewsFragment())
  }
}