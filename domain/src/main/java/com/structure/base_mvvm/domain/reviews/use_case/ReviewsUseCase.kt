package com.structure.base_mvvm.domain.reviews.use_case

//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
import com.structure.base_mvvm.domain.reviews.PaginateDataSource
import com.structure.base_mvvm.domain.reviews.entity.ReviewRequest
import com.structure.base_mvvm.domain.reviews.entity.Reviews
import com.structure.base_mvvm.domain.reviews.repository.ReviewsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Constants.NETWORK_PAGE_SIZE
import com.structure.base_mvvm.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReviewsUseCase @Inject constructor(
  private val reviewsRepository: ReviewsRepository
) {
//  operator fun invoke(instructor_id: Int): Flow<PagingData<Reviews>> =
//    flow {
//      emit(Resource.Loading)
//      val pager = Pager(
//        config = PagingConfig(
//          pageSize = NETWORK_PAGE_SIZE,
//          enablePlaceholders = false
//        ),
//        pagingSourceFactory = {
//          PaginateDataSource(reviewsRepository, instructor_id)
//        }
//      ).flow
//      emit(pager)
//    }.flowOn(Dispatchers.IO)


  operator fun invoke(request: ReviewRequest): Flow<Resource<BaseResponse<*>>> =
    flow {
      if (request.rate.isNotEmpty() && request.review.isNotEmpty()) {
        emit(Resource.Loading)
        val result = reviewsRepository.sendReview(request)
        emit(result)
      }
    }.flowOn(Dispatchers.IO)


}