package com.structure.base_mvvm.presentation.teachers

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentAskPrivateSessionBinding
import com.structure.base_mvvm.presentation.teachers.viewModels.AskPrivateSessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskPrivateSessionFragment : BaseFragment<FragmentAskPrivateSessionBinding>() {

  private val viewModel: AskPrivateSessionViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_ask_private_session

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
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEWS)
        toReviews()
    }
  }

  fun toReviews() {
    navigateSafe(TeacherProfileFragmentDirections.actionToReviewsFragment())
  }
}