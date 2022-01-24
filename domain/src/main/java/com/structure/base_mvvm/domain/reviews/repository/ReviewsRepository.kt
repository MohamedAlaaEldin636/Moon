package com.structure.base_mvvm.domain.reviews.repository

import com.structure.base_mvvm.domain.reviews.entity.ReviewRequest
import com.structure.base_mvvm.domain.reviews.entity.Reviews
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface ReviewsRepository {
  suspend fun reviews(instructor_id: Int): Resource<BaseResponse<List<Reviews>>>
  suspend fun sendReview(request: ReviewRequest): Resource<BaseResponse<*>>

}