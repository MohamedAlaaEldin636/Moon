package grand.app.moon.presentation.reviews

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import grand.app.moon.presentation.reviews.viewModels.ReviewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentReviewsBinding
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReviewsFragment : BaseFragment<FragmentReviewsBinding>() {
  val reviewsFragmentArgs : ReviewsFragmentArgs by navArgs()
  private val viewModel: ReviewsViewModel by viewModels()
  private  val TAG = "ReviewsFragment"

  override
  fun getLayoutId() = R.layout.fragment_reviews

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.request.advertisement_id = reviewsFragmentArgs.advertisementId
    viewModel.getReviews()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      // We use a String here, but any type that can be put in a Bundle is supported
      viewModel.rate = bundle.getString(Constants.RATE).toString()
      // Do something with the result
    }
  }


  override fun setupObservers() {

    viewModel.clickEvent.observe(this,{
      when(it){
        Constants.REVIEW_DIALOG -> {
          findNavController().navigate(ReviewsFragmentDirections.actionReviewsFragmentToReviewDialog(viewModel.request.advertisement_id,viewModel.rate))
        }
      }
    })

    lifecycleScope.launchWhenResumed {
      viewModel.reviewsResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setData(it.value.data)

          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  override fun onDestroy() {
    val response = ReviewsPaginateData()
    response.list.addAll(viewModel.adapter.differ.currentList)
    setFragmentResult(Constants.BUNDLE, bundleOf(Constants.REVIEWS_RESPONSE to response))
    super.onDestroy()
  }
}