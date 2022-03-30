package app.grand.tafwak.presentation.reviews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.presentation.reviews.viewModels.ReviewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentReviewsBinding
import grand.app.moon.presentation.base.BaseFragment
import kotlinx.coroutines.flow.collect

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
//    setFragmentResultListener(Constants.BUNDLE) { requestKey: String, bundle: Bundle ->
//      if(bundle.containsKey("recall")) {
//        viewModel.page = 0
//        viewModel.getReviews()
//      }
//    }
//    setRecyclerViewScrollListener()
//    setFragmentResultListener(Constants.BUNDLE) { _: String, bundle: Bundle ->
//      viewModel.getReviews()
//    }
  }


  private  val TAG = "ReviewsFragment"
  override
  fun setupObservers() {
//    viewModel.clickEvent.observeForever {
//      if (it == Constants.REVIEW_DIALOG)
//        openReviewDialog()
//    }
//    lifecycleScope.launchWhenResumed {
//      viewModel.reviewsResponse.collect {
//        Log.d(TAG, "setupObservers: $it")
//        when (it) {
//          Resource.Loading -> {
//            hideKeyboard()
//            showLoading()
//          }
//          is Resource.Success -> {
//            hideLoading()
//            viewModel.setData(it.value.data)
//          }
//          is Resource.Failure -> {
//            hideLoading()
//            handleApiError(it)
//          }
//        }
//      }
//    }
  }


  private fun setRecyclerViewScrollListener() {

//    val layoutManager = LinearLayoutManager(requireContext())
//    binding.recyclerView.layoutManager = layoutManager
//
//    binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//        super.onScrollStateChanged(recyclerView, newState)
//        if (!recyclerView.canScrollVertically(1)){
//          viewModel.getReviews()
//        }
//      }
//    })
  }



  private fun openReviewDialog() {
//    navigateSafe(ReviewsFragmentDirections.actionReviewsFragmentToReviewDialogComment(viewModel.instructorId))
  }
}