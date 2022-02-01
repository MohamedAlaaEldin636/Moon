package app.grand.tafwak.data.groupDetails.repository

import app.grand.tafwak.data.groupDetails.data_source.GroupDetailsRemoteDataSource
import app.grand.tafwak.domain.groups.entity.GroupDetails
import app.grand.tafwak.domain.groups.repository.GroupDetailsRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(private val remoteDataSource: GroupDetailsRemoteDataSource) :
  GroupDetailsRepository {
  override suspend fun groupDetails(groupId: Int): Resource<BaseResponse<GroupDetails>> =
    remoteDataSource.groupDetails(groupId)
}