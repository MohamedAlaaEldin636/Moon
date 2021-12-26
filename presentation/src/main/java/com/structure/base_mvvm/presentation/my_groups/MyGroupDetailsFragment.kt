package com.structure.base_mvvm.presentation.my_groups

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentMyGroupDetailsBinding
import com.structure.base_mvvm.presentation.my_groups.viewModels.MyGroupDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGroupDetailsFragment : BaseFragment<FragmentMyGroupDetailsBinding>() {

  private val viewModel: MyGroupDetailsViewModel by viewModels()

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