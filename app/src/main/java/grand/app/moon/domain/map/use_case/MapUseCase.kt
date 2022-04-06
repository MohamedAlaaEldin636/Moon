package grand.app.moon.domain.map.use_case

import android.util.Log
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.entity.request.LogInRequest
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.auth.repository.AuthRepository
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

  fun map(
    type: String
  ): Flow<Resource<BaseResponse<List<Store>>>> = flow {
    emit(Resource.Loading)
    val result = repository.map(type)
    emit(result)
  }.flowOn(Dispatchers.IO)

}