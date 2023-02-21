package grand.app.moon.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import com.google.maps.android.ui.IconGenerator
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import kotlin.math.roundToInt

fun Context.createRectangleShape(
	cornerInDp: Float? = null,
	sizeInDp: Float? = null,
	@ColorRes colorRes: Int? = null
): GradientDrawable {
	val corner = cornerInDp?.let { dpToPx(cornerInDp) }
	val size = sizeInDp?.let { dpToPx(sizeInDp).roundToInt() }

	val shape = GradientDrawable()
	shape.shape = GradientDrawable.RECTANGLE
	if (corner != null) {
		shape.cornerRadius = corner
	}
	if (size != null) {
		shape.setSize(size, size)
	}
	if (colorRes != null) {
		shape.setColor(ContextCompat.getColor(this, colorRes))
	}
	return shape
}

fun View.toBitmapUsingIconGenerator(background: Drawable): Bitmap? {
	val iconGenerator = IconGenerator(context)
	iconGenerator.setContentPadding(0, 0, 0, 0)
	//ColorDrawable(Color.BLACK)
	iconGenerator.setBackground(background)
	iconGenerator.setContentView(this)
	//iconGenerator.setStyle(this)
	//iconGenerator.setColor(this)
	return iconGenerator.makeIcon()
}
fun View.toBitmapUsingDraw(fixedSize: Int): Bitmap? {
	measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY)

	val bitmap = Bitmap.createBitmap(fixedSize, fixedSize, Bitmap.Config.ARGB_8888)

	val canvas = Canvas(bitmap)

	layout(0, 0, fixedSize, fixedSize)

	draw(canvas)

	return bitmap
}

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
