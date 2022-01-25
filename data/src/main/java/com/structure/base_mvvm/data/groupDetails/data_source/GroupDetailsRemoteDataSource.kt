package com.structure.base_mvvm.data.groupDetails.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class GroupDetailsRemoteDataSource @Inject constructor(private val apiService: GroupDetailsServices) :
  BaseRemoteDataSource() {

  suspend fun groupDetails(groupId: Int) = safeApiCall {
    apiService.groupDetails(groupId)
  }
}