package com.structure.base_mvvm.data.reviews.repository

import com.structure.base_mvvm.data.reviews.data_source.ReviewsRemoteDataSource
import com.structure.base_mvvm.domain.reviews.entity.ReviewRequest
import com.structure.base_mvvm.domain.reviews.entity.Reviews
import com.structure.base_mvvm.domain.reviews.entity.ReviewsMain
import com.structure.base_mvvm.domain.reviews.repository.ReviewsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(private val remoteDataSource: ReviewsRemoteDataSource) :
  ReviewsRepository {
  override suspend fun reviews(instructor_id: Int, page: Int): Resource<BaseResponse<ReviewsMain>> =
    remoteDataSource.reviews(instructor_id, page)

  override suspend fun sendReview(request: ReviewRequest): Resource<BaseResponse<*>> =
    remoteDataSource.sendReview(request)

}