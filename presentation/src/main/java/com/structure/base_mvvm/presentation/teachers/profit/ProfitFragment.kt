package com.structure.base_mvvm.presentation.teachers.profit

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.databinding.FragmentProfitBinding
import com.structure.base_mvvm.presentation.teachers.profit.viewModels.ProfitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfitFragment : BaseFragment<FragmentProfitBinding>() {

  private val viewModel: ProfitViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_profit

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
}