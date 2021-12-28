package com.structure.base_mvvm.presentation.reviews

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.getMyColor
import com.structure.base_mvvm.presentation.base.extensions.getMyString
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.databinding.FragmentReviewsBinding
import com.structure.base_mvvm.presentation.databinding.FragmentTeachersBinding
import com.structure.base_mvvm.presentation.databinding.ReviewDialogBinding
import com.structure.base_mvvm.presentation.home.HomeFragmentDirections
import com.structure.base_mvvm.presentation.reviews.viewModels.ReviewsViewModel
import com.structure.base_mvvm.presentation.teachers.viewModels.TeachersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReviewsFragment : BaseFragment<FragmentReviewsBinding>() {

  private val viewModel: ReviewsViewModel by viewModels()

  private lateinit var dialog: Dialog

  override
  fun getLayoutId() = R.layout.fragment_reviews

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

  override
  fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEW_DIALOG)
        openReviewDialog()
    }
  }

  private fun openReviewDialog() {
//    dialog = Dialog(requireActivity(), R.style.PauseDialog)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
//    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    val binding: ReviewDialogBinding = DataBindingUtil.inflate(
//      LayoutInflater.from(dialog.context),
//      R.layout.review_dialog,
//      null,
//      false
//    )
//    dialog.setContentView(binding.root)
//    binding.viewModel = viewModel
//    dialog.show()
    viewModel.test = 5
    navigateSafe(ReviewsFragmentDirections.actionReviewsFragmentToReviewDialogComment(viewModel.test))
  }
}