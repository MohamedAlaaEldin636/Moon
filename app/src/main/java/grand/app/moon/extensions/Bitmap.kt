package grand.app.moon.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.toBitmap(fixedSize: Int? = null): Bitmap? = toBitmap(fixedSize, fixedSize)
fun View.toBitmap(fixedWidth: Int? = null, fixedHeight: Int? = null): Bitmap? {
	measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

	val bitmap = Bitmap.createBitmap(fixedWidth ?: measuredWidth, fixedHeight ?: measuredHeight, Bitmap.Config.ARGB_8888)

	val canvas = Canvas(bitmap)

	layout(0, 0, fixedWidth ?: measuredWidth, fixedHeight ?: measuredHeight)

	draw(canvas)
	/*
	view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
					Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(bitmap);
	view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	view.draw(canvas);
	return bitmap;
	 */

	return bitmap
}
