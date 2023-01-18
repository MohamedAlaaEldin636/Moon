package grand.app.moon.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getDrawableOrTransparent(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId).orTransparent()

fun Drawable?.orTransparent() = this ?: ColorDrawable(Color.TRANSPARENT)

@ColorInt
fun Int?.orTransparentColor() = this ?: Color.TRANSPARENT
