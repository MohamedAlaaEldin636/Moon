package app.grand.tafwak.data.reviews.repository

import app.grand.tafwak.data.reviews.data_source.ReviewsRemoteDataSource
import app.grand.tafwak.domain.reviews.entity.ReviewRequest
import app.grand.tafwak.domain.reviews.entity.ReviewsMain
import app.grand.tafwak.domain.reviews.repository.ReviewsRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(private val remoteDataSource: ReviewsRemoteDataSource) :
  ReviewsRepository {
  override suspend fun reviews(instructor_id: Int, page: Int): Resource<BaseResponse<ReviewsMain>> =
    remoteDataSource.reviews(instructor_id, page)

  override suspend fun sendReview(request: ReviewRequest): Resource<BaseResponse<*>> =
    remoteDataSource.sendReview(request)

}