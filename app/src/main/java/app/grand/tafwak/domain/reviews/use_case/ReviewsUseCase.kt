package app.grand.tafwak.domain.reviews.use_case

//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
import app.grand.tafwak.domain.reviews.entity.ReviewRequest
import app.grand.tafwak.domain.reviews.repository.ReviewsRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
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