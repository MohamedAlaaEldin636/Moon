package com.structure.base_mvvm.domain.reviews.use_case

import com.structure.base_mvvm.domain.reviews.entity.ReviewRequest
import com.structure.base_mvvm.domain.reviews.entity.ReviewValidationException
import com.structure.base_mvvm.domain.reviews.entity.Reviews
import com.structure.base_mvvm.domain.reviews.repository.ReviewsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReviewsUseCase @Inject constructor(
  private val reviewsRepository: ReviewsRepository
) {
  operator fun invoke(instructor_id: Int): Flow<Resource<BaseResponse<List<Reviews>>>> =
    flow {
      emit(Resource.Loading)
      val result = reviewsRepository.reviews(instructor_id)
      emit(result)
    }.flowOn(Dispatchers.IO)

  @Throws(ReviewValidationException::class)
  operator fun invoke(request: ReviewRequest): Flow<Resource<BaseResponse<*>>> =
    flow {
      if (request.rate.isNotEmpty() && request.review.isNotEmpty()) {
        emit(Resource.Loading)
        val result = reviewsRepository.sendReview(request)
        emit(result)
      }
    }.flowOn(Dispatchers.IO)

}