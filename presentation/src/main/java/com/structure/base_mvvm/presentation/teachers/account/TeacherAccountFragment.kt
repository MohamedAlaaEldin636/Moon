package com.structure.base_mvvm.presentation.teachers.account

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.auth.AuthActivity
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.databinding.FragmentTeacherAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TeacherAccountFragment : BaseFragment<FragmentTeacherAccountBinding>() {

  private val viewModel: TeacherAccountViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_teacher_account

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.account)
//    binding.includedToolbar.backIv.hide()
  }

  override
  fun setupObservers() {
    viewModel.showLogOutPopUp.observe(this) { showLogOutPopUp() }
  }

  private fun showLogOutPopUp() {
    PrettyPopUpHelper.Builder(childFragmentManager)
      .setStyle(PrettyPopUpHelper.Style.STYLE_1_HORIZONTAL_BUTTONS)
      .setTitle(R.string.log_out)
      .setTitleColor(getMyColor(R.color.colorPrimaryDark))
      .setContent(R.string.log_out_hint)
      .setContentColor(getMyColor(R.color.gray))
      .setPositiveButtonBackground(R.drawable.btn_accent)
      .setPositiveButtonTextColor(getMyColor(R.color.colorPrimaryDark))
      .setImage(R.drawable.alerter_ic_notifications)
      .setPositiveButton(R.string.log_out) {
        it.dismiss()
        logOut()
      }
      .setNegativeButtonBackground(R.drawable.btn_gray)
      .setNegativeButtonTextColor(getMyColor(R.color.white))
      .setNegativeButton(getMyString(R.string.cancel), null)
      .create()
  }

  private fun logOut() {
    viewModel.logOut()

    lifecycleScope.launchWhenResumed {
      viewModel.logOutResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openLogInScreen()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  private fun openLogInScreen() {
    requireActivity().openActivityAndClearStack(AuthActivity::class.java)
  }
}