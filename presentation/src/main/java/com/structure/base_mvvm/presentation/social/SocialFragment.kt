package com.structure.base_mvvm.presentation.social

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.databinding.FragmentSocialBinding
import com.structure.base_mvvm.presentation.social.viewModels.SocialViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SocialFragment : BaseFragment<FragmentSocialBinding>() {

  private val viewModel: SocialViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_social

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.social)
    binding.includedToolbar.backIv.show()
    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen() }
  }

  override fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel.socialResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.updateSocial(it.value.data)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }
}