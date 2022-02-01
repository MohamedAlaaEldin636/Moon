package app.grand.tafwak.domain.reviews.repository

import app.grand.tafwak.domain.reviews.entity.ReviewRequest
import app.grand.tafwak.domain.reviews.entity.ReviewsMain
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface ReviewsRepository {
  suspend fun reviews(instructor_id: Int,page:Int): Resource<BaseResponse<ReviewsMain>>
  suspend fun sendReview(request: ReviewRequest): Resource<BaseResponse<*>>

}