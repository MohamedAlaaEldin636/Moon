package com.structure.base_mvvm.presentation.social

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.backToPreviousScreen
import com.structure.base_mvvm.presentation.base.extensions.getMyString
import com.structure.base_mvvm.presentation.base.extensions.show
import com.structure.base_mvvm.presentation.databinding.FragmentAboutBinding
import com.structure.base_mvvm.presentation.databinding.FragmentPrivacyBinding
import com.structure.base_mvvm.presentation.privacy.viewModels.PrivacyViewModel
import com.structure.base_mvvm.presentation.social.viewModels.SocialViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment : BaseFragment<FragmentPrivacyBinding>() {

  private val viewModel: SocialViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_privacy

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.privacy)
    binding.includedToolbar.backIv.show()
    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen()}
  }
}