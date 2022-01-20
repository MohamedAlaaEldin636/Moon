package com.structure.base_mvvm.presentation.my_groups

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentMyGroupsBinding
import com.structure.base_mvvm.presentation.my_groups.viewModels.MyGroupsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGroupsFragment : BaseFragment<FragmentMyGroupsBinding>() {

  private val viewModel: MyGroupsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_my_groups

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