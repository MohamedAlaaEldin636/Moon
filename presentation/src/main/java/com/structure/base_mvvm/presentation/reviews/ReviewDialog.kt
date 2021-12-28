package com.structure.base_mvvm.presentation.reviews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.databinding.ReviewDialogBinding
import com.structure.base_mvvm.presentation.reviews.viewModels.ReviewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewDialog : DialogFragment() {
  private val viewModel: ReviewsViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val binding: ReviewDialogBinding =
      DataBindingUtil.inflate(inflater, R.layout.review_dialog, container, false)
    binding.viewModel = viewModel
    binding.btnRate.setOnClickListener {
      viewModel.test = 15
      val bundle = Bundle()
      bundle.putInt("value", viewModel.test)
      setFragmentResult(Constants.BUNDLE, bundle)
      dismiss()
    }
    return binding.root
  }
}