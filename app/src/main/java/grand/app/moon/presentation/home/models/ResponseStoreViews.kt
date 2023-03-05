package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName

data class ResponseStoreViews(
	var id: Int?,
	var image: String?,
	var name: String?,
	@SerializedName("total_views") var totalViews: Int?,
	@SerializedName("created_at") var createdAt: String?,
)
