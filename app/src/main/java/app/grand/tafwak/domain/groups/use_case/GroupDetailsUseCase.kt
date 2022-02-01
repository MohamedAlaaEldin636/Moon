package app.grand.tafwak.domain.groups.use_case

import app.grand.tafwak.domain.groups.entity.GroupDetails
import app.grand.tafwak.domain.groups.repository.GroupDetailsRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GroupDetailsUseCase @Inject constructor(
  private val groupDetailsRepository: GroupDetailsRepository
) {
  operator fun invoke(groupId: Int): Flow<Resource<BaseResponse<GroupDetails>>> =
    flow {
      emit(Resource.Loading)
      val result = groupDetailsRepository.groupDetails(groupId)
      emit(result)
    }.flowOn(Dispatchers.IO)

}