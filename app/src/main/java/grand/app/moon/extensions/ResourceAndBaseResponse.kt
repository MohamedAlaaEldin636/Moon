package grand.app.moon.extensions

import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.domain.utils.map

fun <T> Resource<BaseResponse<*>>.mapToNullSuccess(): Resource<BaseResponse<T?>> =
	mapSuccess { it.map { null } }
