package com.structure.base_mvvm.domain.groups.use_case

import com.structure.base_mvvm.domain.groups.entity.GroupDetails
import com.structure.base_mvvm.domain.groups.repository.GroupDetailsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
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