package grand.app.moon.presentation.reviews

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.BR
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.databinding.FragmentReviewsBinding
import grand.app.moon.presentation.reviews.viewModels.ReviewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : BaseFragment<FragmentReviewsBinding>() {
  private val viewModel: ReviewsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_reviews

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
    setFragmentResultListener(Constants.BUNDLE) { _: String, bundle: Bundle ->
      viewModel.getReviews()
    }
  }


  override
  fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEW_DIALOG)
        openReviewDialog()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.reviewsResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.reviewsAdapter.differ.submitList(it.value.data)
            viewModel.notifyPropertyChanged(BR.reviewsAdapter)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
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

    navigateSafe(ReviewsFragmentDirections.actionReviewsFragmentToReviewDialogComment(viewModel.instructorId))
  }
}