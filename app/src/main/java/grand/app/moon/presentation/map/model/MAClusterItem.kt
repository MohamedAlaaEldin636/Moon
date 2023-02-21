package grand.app.moon.presentation.map.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MAClusterItem(
	val id: Int,
	val latitude: Double,
	val longitude: Double,
	val titleOfItem: String? = null,
	val snippetOfItem: String? = null,
) : ClusterItem {

	private val latLng = LatLng(latitude, longitude)

	override fun getPosition(): LatLng {
		return latLng
	}

	override fun getTitle(): String? = titleOfItem

	override fun getSnippet(): String? = snippetOfItem

}
