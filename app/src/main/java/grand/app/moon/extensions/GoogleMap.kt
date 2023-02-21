package grand.app.moon.extensions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

fun LatLng?.orZero() = this ?: LatLng(0.0, 0.0)

fun List<LatLng>.toLatLngBounds(): LatLngBounds {
	val boundsBuilder = LatLngBounds.Builder()

	for (item in this) {
		boundsBuilder.include(item)
	}

	return boundsBuilder.build()
}
