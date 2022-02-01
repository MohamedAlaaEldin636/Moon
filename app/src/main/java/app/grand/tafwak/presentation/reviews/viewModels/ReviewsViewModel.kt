package app.grand.tafwak.presentation.reviews.viewModels

import android.widget.RatingBar
import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.reviews.entity.ReviewRequest
import app.grand.tafwak.domain.reviews.entity.Reviews
import app.grand.tafwak.domain.reviews.use_case.ReviewsUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
  private val reviewsUseCase: ReviewsUseCase,
  savedStateHandle: SavedStateHandle
) : BaseViewModel() {
  @Bindable
  val reviewsAdapter: ReviewsAdapter = ReviewsAdapter()
  var request: ReviewRequest = ReviewRequest()
  private val _reviewsResponse =
    MutableStateFlow<Resource<BaseResponse<List<Reviews>>>>(Resource.Default)
  val reviewsResponse = _reviewsResponse
  private val _reviewDialogResponse =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val reviewDialogResponse = _reviewDialogResponse
  var validationException = SingleLiveEvent<Int>()

  var instructorId: Int = 0

  init {
    savedStateHandle.get<Int>("instructor_id")?.let { instructor_id ->
      this.instructorId = instructor_id
      getReviews()
    }
  }

  fun getReviews() {
//    reviewsUseCase.invoke(instructorId)
//      .onEach { result ->
//        println(result.toString())
////        _reviewsResponse.value = result
//      }
//      .launchIn(viewModelScope)
  }

  fun sendRate() {
    request.instructor_id = instructorId
    reviewsUseCase.invoke(request)
      .onEach { result ->
        println(result.toString())
        _reviewDialogResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  fun onRateChange(ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
    request.rate = rating.toString()
  }
}