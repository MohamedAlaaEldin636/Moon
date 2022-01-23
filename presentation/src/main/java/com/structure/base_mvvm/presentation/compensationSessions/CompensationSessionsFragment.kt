package com.structure.base_mvvm.presentation.compensationSessions

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.compensationSessions.viewModels.CompensationSessionsViewModel
import com.structure.base_mvvm.presentation.databinding.FragmentCompensationSessionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompensationSessionsFragment : BaseFragment<FragmentCompensationSessionsBinding>() {

  private val viewModel: CompensationSessionsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_compensation_sessions

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