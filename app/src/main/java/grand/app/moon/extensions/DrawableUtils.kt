package grand.app.moon.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

fun Drawable?.orTransparent() = this ?: ColorDrawable(Color.TRANSPARENT)

@ColorInt
fun Int?.orTransparentColor() = this ?: Color.TRANSPARENT
