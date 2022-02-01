package app.grand.tafwak.domain.reviews

import app.grand.tafwak.domain.reviews.repository.ReviewsRepository
import javax.inject.Inject

class PaginateDataSource @Inject constructor(
  private val reviewsRepository: ReviewsRepository,
  private val query: Int
)
//  : PagingSource<Int, Reviews>() {
//  override fun getRefreshKey(state: PagingState<Int, Reviews>): Int {
//    TODO("Not yet implemented")
//  }
//
//  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Reviews> {
//    val pageIndex = params.key ?: Constants.PAGE_INDEX
//    return try {
//      val response = reviewsRepository.reviews(
//        instructor_id = query,
//        page = pageIndex
//      )
//      if (response is Resource.Success) {
//        val nextKey =
//          if (response.value.data.reviews.isEmpty()) {
//            null
//          } else {
//            // By default, initial load size = 3 * NETWORK PAGE SIZE
//            // ensure we're not requesting duplicating items at the 2nd request
//            pageIndex + (params.loadSize / Constants.NETWORK_PAGE_SIZE)
//          }
//        LoadResult.Page(
//          data = response.value.data.reviews,
//          prevKey = if (pageIndex == Constants.PAGE_INDEX) null else pageIndex,
//          nextKey = nextKey
//        )
//      } else {
//        return LoadResult.Page(emptyList(), 1, 1)
//      }
//    } catch (exception: IOException) {
//      return LoadResult.Error(exception)
//    } catch (exception: HttpException) {
//      return LoadResult.Error(exception)
//    }
//  }
//}