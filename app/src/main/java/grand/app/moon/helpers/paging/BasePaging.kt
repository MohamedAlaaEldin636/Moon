package grand.app.moon.helpers.paging

import androidx.paging.*
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.toFailureStatus
import kotlinx.coroutines.flow.Flow

suspend fun <T : Any> BasePaging.Companion.getAllPages(
	fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
): Resource<BaseResponse<List<T>?>> {
	var page = 1
	val list = mutableListOf<T>()
	val message: String?
	val code: Int?
	while (true) {
		when (val response = fetchPage(page++)) {
			is MAResult.Failure -> {
				return Resource.Failure(
					response.toFailureStatus(),
					response.code,
					response.message,
				)
			}
			is MAResult.Success -> {
				list += response.value.data?.data.orEmpty()

				if (response.value.data?.links?.next.isNullOrEmpty()) {
					message = response.value.message
					code = response.value.code

					break
				}
			}
		}
	}

	return Resource.Success(
		BaseResponse(
			list.toList(),
			message.orEmpty(),
			code ?: 200
		)
	)
}

class BasePaging<T : Any>(
	private val fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
) : PagingSource<Int, T>() {
	
	companion object {
		fun <T : Any> createFlowViaPager(
			fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
		): Flow<PagingData<T>> {
			return Pager(
				PagingConfig(10, enablePlaceholders = false)
			) {
				BasePaging {
					fetchPage(it)
				}
			}.flow
		}
	}
	
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val pageNumber = params.key ?: 1 // As 1 is the minimum page number.
		
		return when (val result = fetchPage(pageNumber)) {
			is MAResult.Success -> LoadResult.Page(
				result.value.data?.data.orEmpty(),
				if (pageNumber == 1) null else pageNumber.dec(),
				if (result.value.data?.links?.next.isNullOrEmpty()) null else pageNumber.inc()
			)
			is MAResult.Failure -> LoadResult.Error(MAPagingException(result))
		}
	}
	
	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}
	
}

/*class BasePagingForChat<T : Any>(
	private val fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>,
	private val getLastPage: suspend () -> Int,
) : PagingSource<Int, T>() {
	
	companion object {
		fun <T : Any> createFlowViaPager(
			fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
		): Flow<PagingData<T>> {
			return Pager(
				PagingConfig(10, enablePlaceholders = false)
			) {
				BasePaging {
					fetchPage(it)
				}
			}.flow
		}
	}
	
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val pageNumber = params.key ?: getLastPage()
		
		return when (val result = fetchPage(pageNumber)) {
			is MAResult.Success -> LoadResult.Page(
				result.value.data?.data.orEmpty(),
				if (pageNumber == 1) null else pageNumber.dec(),
				if (result.value.data?.nextPageUrl == null) null else pageNumber.inc()
			)
			is MAResult.Failure -> LoadResult.Error(MAPagingException(result))
		}
	}
	
	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}
	
}*/
