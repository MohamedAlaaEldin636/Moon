package grand.app.moon.domain.map.use_case

import android.util.Log
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.auth.repository.AuthRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.map.repository.MapRepository
import grand.app.moon.domain.utils.*
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class MapUseCase @Inject constructor(
  private val repository: MapRepository,
) {

  fun mapStore(
    type: String,
    category_id: Int?,
    sub_category_id: Int?,
    property_id: Int?,
  ): Flow<Resource<BaseResponse<List<Store>>>> = flow {
    emit(Resource.Loading)
    val result = repository.mapStore(type,category_id,sub_category_id,property_id)
    emit(result)
  }.flowOn(Dispatchers.IO)


  fun mapAds(
    type: String,
    category_id: String?,
    sub_category_id: String?,
    property_id: String?
  ): Flow<Resource<BaseResponse<List<Advertisement>>>> = flow {
    emit(Resource.Loading)
    val result = repository.mapAds(type,property_id,sub_category_id,category_id)
    emit(result)
  }.flowOn(Dispatchers.IO)

}