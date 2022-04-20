package grand.app.moon.presentation.reviews

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.presentation.reviews.viewModels.ReviewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentReviewsBinding
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.StoreListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReviewsFragment : BaseFragment<FragmentReviewsBinding>() {
  val reviewsFragmentArgs : ReviewsFragmentArgs by navArgs()
  private val viewModel: ReviewsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_reviews

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.request.advertisement_id = reviewsFragmentArgs.advertisementId
    viewModel.getReviews()
  }


  private  val TAG = "StoreListFragment"
  override fun setupObservers() {

    viewModel.clickEvent.observe(this,{
      when(it){
        Constants.REVIEW_DIALOG -> {

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

  private fun openReviewDialog() {
//    navigateSafe(ReviewsFragmentDirections.actionReviewsFragmentToReviewDialogComment(viewModel.instructorId))
  }
}