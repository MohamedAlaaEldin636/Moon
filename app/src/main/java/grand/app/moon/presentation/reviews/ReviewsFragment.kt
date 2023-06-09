package grand.app.moon.presentation.reviews

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import grand.app.moon.core.extenstions.actOnGetIfNotInitialValueOrGetLiveData
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

@AndroidEntryPoint
class ReviewsFragment : BaseFragment<FragmentReviewsBinding>() {
  private val viewModel: ReviewsViewModel by viewModels()
  private val TAG = "ReviewsFragment"

  override
  fun getLayoutId() = R.layout.fragment_reviews

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    requireArguments().let {
      if(requireArguments().containsKey("advertisement_id")  && requireArguments().getInt("advertisement_id") != -1) {
        viewModel.request.advertisement_id = requireArguments().getInt("advertisement_id").toString()
      }
      if(requireArguments().containsKey("store_id") && requireArguments().getInt("store_id") != -1 ) {
        viewModel.request.store_id = requireArguments().getInt("store_id").toString()
      }
    }
    viewModel.getReviews()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    actOnGetIfNotInitialValueOrGetLiveData(
      Constants.REVIEW,
      false,
      viewLifecycleOwner,
      { it == true }
    ) {
//      backToPreviousScreen()
      val n = findNavController()
      n.navigateUp()
      n.currentBackStackEntry?.savedStateHandle?.set(
        Constants.REVIEW,
        true
      )
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
//      if(bundle.containsKey(Constants.REVIEW)) {
//        backToPreviousScreen()
//        bundle.remove(Constants.BUNDLE)
//      }
//      // We use a String here, but any type that can be put in a Bundle is supported
////      viewModel.adapter.add(bundle.getSerializable(Constants.REVIEW) as Reviews)
//      // Do something with the result
//    }
//

  }


  override fun setupObservers() {

    viewModel.clickEvent.observe(this, {
      when (it) {
        Constants.REVIEW_DIALOG -> {
          val action = ReviewsFragmentDirections.actionReviewsFragmentToReviewDialog(viewModel.rate)
          Log.d(TAG, "setupObservers: ${viewModel.request.advertisement_id}")
          Log.d(TAG, "setupObservers: ${viewModel.request.store_id}")
          viewModel.request.advertisement_id?.let {
            action.advertisementId = it.toInt()
          }
          viewModel.request.store_id?.let {
            action.storeId = it.toInt()
          }
          findNavController().navigate(action)
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
	        else -> {}
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