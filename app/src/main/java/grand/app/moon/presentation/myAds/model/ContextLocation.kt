package grand.app.moon.presentation.myAds.model

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import grand.app.moon.R
import grand.app.moon.extensions.MyLogger
import java.util.*

@WorkerThread
fun Context.getAddressFromLatitudeAndLongitude(
	latitude: Double,
	longitude: Double,
	fallbackAddress: String = getString(R.string.your_address_has_been_selected_successfully),
): String {
    val address = try {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1).orEmpty()
        if (addresses.isNotEmpty()) {
            addresses[0].getAddressLine(0).also {
                MyLogger.e(it)
            }
        }else {
            MyLogger.e("address NULL")

            null
        }
    }catch (throwable: Throwable) {
		    MyLogger.e(throwable)

        null
    }

    return address ?: fallbackAddress
}

fun Context.checkSelfPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
