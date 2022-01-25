package com.structure.base_mvvm.data.groupDetails.repository

import com.structure.base_mvvm.data.groupDetails.data_source.GroupDetailsRemoteDataSource
import com.structure.base_mvvm.domain.groups.entity.GroupDetails
import com.structure.base_mvvm.domain.groups.repository.GroupDetailsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(private val remoteDataSource: GroupDetailsRemoteDataSource) :
  GroupDetailsRepository {
  override suspend fun groupDetails(groupId: Int): Resource<BaseResponse<GroupDetails>> =
    remoteDataSource.groupDetails(groupId)
}