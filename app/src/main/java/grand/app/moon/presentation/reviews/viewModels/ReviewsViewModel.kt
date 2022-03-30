package app.grand.tafwak.presentation.reviews.viewModels

import android.util.Log
import android.widget.RatingBar
import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
  private val reviewsUseCase: AdsUseCase,
  savedStateHandle: SavedStateHandle
) : BaseViewModel() {

//  @Bindable
//  var page: Int = 0
//  @Bindable
//  var callingService = false
//  var isLast = false
//  @Bindable
//  val adapter: ReviewsAdapter = ReviewsAdapter()
//
//  var request: ReviewRequest = ReviewRequest()
//  private val _reviewsResponse =
//    MutableStateFlow<Resource<BaseResponse<ReviewsPaginateData>>>(Resource.Default)
//  val reviewsResponse = _reviewsResponse
//  private val _reviewDialogResponse =
//    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
//  val reviewDialogResponse = _reviewDialogResponse
//  var validationException = SingleLiveEvent<Int>()
//
//  var instructorId: Int = 0
//
//  protected val job: Job by lazy{
//    Job()
//  }
//
//
//  private val TAG = "ReviewsViewModel"
//  init {
//    Log.d(TAG, ": ReviewsViewModel")
//    savedStateHandle.get<Int>("instructor_id")?.let { instructor_id ->
//      this.instructorId = instructor_id
//      Log.d(TAG, ": $instructorId")
//      getReviews()
//    }
//  }
//
//  fun getReviews() {
//
//
//    if (!callingService && !isLast) {
//      callingService = true
//      notifyPropertyChanged(BR.callingService)
//      viewModelScope.launch(job) {
//        page++
//        if(page > 1){
//          notifyPropertyChanged(BR.page)
//        }
//        reviewsUseCase.getReviews(instructorId,page)
//          .onEach { result ->
//            println(result.toString())
//            _reviewsResponse.value = result
//          }
//          .launchIn(viewModelScope)
//
//      }
//    }
//
//  }
//
//  fun sendRate() {
//    request.instructor_id = instructorId
//    reviewsUseCase.invoke(request)
//      .onEach { result ->
//        println(result.toString())
//        _reviewDialogResponse.value = result
//      }
//      .launchIn(viewModelScope)
//  }
//
//
//  lateinit var result: ReviewsPaginateData
//  fun setData(data: ReviewsPaginateData?) {
//    data?.let {
//      result = data
//      println("size:" + data.list.size)
//      isLast = data.links.next == null
//      if (page == 1) {
////        adapter = InvoicesAdapter()
//        adapter.differ.submitList(it.list)
//      } else {
//        adapter.insertData(it.list)
//      }
//      notifyPropertyChanged(BR.adapter)
//      callingService = false
//      notifyPropertyChanged(BR.callingService)
//    }
//  }
//
//
//
//  fun onRateChange(ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
//    request.rate = rating.toString()
//  }
}