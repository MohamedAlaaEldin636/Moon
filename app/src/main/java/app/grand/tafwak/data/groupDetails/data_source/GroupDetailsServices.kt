package app.grand.tafwak.data.groupDetails.data_source

import app.grand.tafwak.domain.groups.entity.GroupDetails
import app.grand.tafwak.domain.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupDetailsServices {
  @GET("v1/classes/{group_id}")
  suspend fun groupDetails(@Path("group_id") groupId: Int): BaseResponse<GroupDetails>

}