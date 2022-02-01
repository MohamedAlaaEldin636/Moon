package app.grand.tafwak.data.groupDetails.data_source

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class GroupDetailsRemoteDataSource @Inject constructor(private val apiService: GroupDetailsServices) :
  BaseRemoteDataSource() {

  suspend fun groupDetails(groupId: Int) = safeApiCall {
    apiService.groupDetails(groupId)
  }
}