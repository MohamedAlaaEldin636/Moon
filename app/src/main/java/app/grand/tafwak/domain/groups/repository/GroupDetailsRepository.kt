package app.grand.tafwak.domain.groups.repository

import app.grand.tafwak.domain.groups.entity.GroupDetails
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface GroupDetailsRepository {
  suspend fun groupDetails(groupId: Int): Resource<BaseResponse<GroupDetails>>
}