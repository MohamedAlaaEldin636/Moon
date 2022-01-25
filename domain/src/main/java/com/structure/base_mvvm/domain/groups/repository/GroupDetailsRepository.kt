package com.structure.base_mvvm.domain.groups.repository

import com.structure.base_mvvm.domain.groups.entity.GroupDetails
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface GroupDetailsRepository {
  suspend fun groupDetails(groupId: Int): Resource<BaseResponse<GroupDetails>>
}