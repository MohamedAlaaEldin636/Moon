package app.grand.tafwak.data.reviews.data_source

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import app.grand.tafwak.domain.reviews.entity.ReviewRequest
import javax.inject.Inject

class ReviewsRemoteDataSource @Inject constructor(private val apiService: ReviewsServices) :
  BaseRemoteDataSource() {

  suspend fun reviews(instructor_id: Int, page: Int) = safeApiCall {
    apiService.reviews(instructor_id, page)
  }

  suspend fun sendReview(request: ReviewRequest) = safeApiCall {
    apiService.sendReview(request.instructor_id, request)
  }

}