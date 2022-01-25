package com.structure.base_mvvm.data.groupDetails.data_source

import com.structure.base_mvvm.domain.groups.entity.GroupDetails
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupDetailsServices {
  @GET("v1/classes/{group_id}")
  suspend fun groupDetails(@Path("group_id") groupId: Int): BaseResponse<GroupDetails>

}