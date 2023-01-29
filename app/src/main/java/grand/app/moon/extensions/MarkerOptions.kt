package grand.app.moon.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions

fun MarkerOptions.iconDrawableRes(context: Context?, @DrawableRes res: Int) = run {
	if (context == null) return@run this

	val bitmapDescriptor = ContextCompat.getDrawable(context, res).orTransparent().run {
		setBounds(0, 0, intrinsicWidth, intrinsicHeight)
		val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
		draw(Canvas(bitmap))
		BitmapDescriptorFactory.fromBitmap(bitmap)
	}

	icon(
		bitmapDescriptor
	)
}
