package grand.app.moon.domain.shop

import com.google.gson.annotations.SerializedName

data class ResponseStoreSubCategory(
	var id: Int?,
	var name: String?,
	@SerializedName("parent_id") var parentId: Int?,
)
