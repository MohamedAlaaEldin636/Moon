package grand.app.moon.extensions

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import grand.app.moon.R

fun Fragment.changeStatusBarToWhiteBackgroundDarkIcons() = changeStatusBarCustomColor(Color.WHITE, true)
fun Fragment.changeStatusBarToDefaultBackgroundAndIcons() {
    context?.also { context ->
        changeStatusBarCustomColor(ContextCompat.getColor(context, R.color.colorPrimaryDark), false)
    }
}

fun Fragment.makeStatusBarTransparentWithDarkIcons() = changeStatusBarCustomColor(Color.TRANSPARENT, true)
fun Fragment.makeStatusBarTransparentWithWhiteIcons() = changeStatusBarCustomColor(Color.TRANSPARENT, false)
fun Activity.makeStatusBarTransparentWithWhiteIcons() = changeStatusBarCustomColor(Color.TRANSPARENT, false)
fun Activity.makeStatusBarTransparentWithDarkIcons() = changeStatusBarCustomColor(Color.TRANSPARENT, true)

fun Fragment.makeStatusBarToPrimaryDarkWithWhiteIcons() = changeStatusBarCustomColor(
    context?.getColor(R.color.colorPrimaryDark).orTransparentColor(),
    false
)

fun Fragment.makeStatusBarToPrimaryWithWhiteIcons() = changeStatusBarCustomColor(
    context?.getColor(R.color.colorPrimary).orTransparentColor(),
    false
)

fun Fragment.makeStatusBarToWhiteWithDarkIcons() = changeStatusBarCustomColor(
    Color.WHITE,
    true
)

fun Fragment.changeStatusBarCustomColor(@ColorInt color: Int, useDarkIcons: Boolean) {
    activity?.window?.apply {
        kotlin.runCatching {
            if (color == Color.TRANSPARENT) {
                setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
		            statusBarColor = color
            }else {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = color
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && insetsController != null) {
                val compat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(insetsController!!)

                compat.isAppearanceLightStatusBars = useDarkIcons

                compat.show(WindowInsetsCompat.Type.statusBars())
            }else {
                if (useDarkIcons) {
                    decorView.systemUiVisibility = decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }else {
                    decorView.systemUiVisibility = decorView.systemUiVisibility and
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }
}

fun Activity.changeStatusBarCustomColor(@ColorInt color: Int, useDarkIcons: Boolean) {
    window?.apply {
        kotlin.runCatching {
            if (color == Color.TRANSPARENT) {
                setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }else {
                //clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = color
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && insetsController != null) {
                val compat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(insetsController!!)

                compat.isAppearanceLightStatusBars = useDarkIcons

                compat.show(WindowInsetsCompat.Type.statusBars())
            }else {
                if (useDarkIcons) {
                    decorView.systemUiVisibility = decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }else {
                    decorView.systemUiVisibility = decorView.systemUiVisibility and
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }
}

/*fun Fragment.clearStatusBarCustomColor(@ColorInt color: Int, useDarkIcons: Boolean) {
    activity?.window?.apply {
        //clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && insetsController != null) {
            val compat = WindowInsetsControllerCompat.toWindowInsetsControllerCompat(insetsController!!)

            compat.isAppearanceLightStatusBars = true

            compat.show(WindowInsetsCompat.Type.statusBars())
        }else {
            decorView.systemUiVisibility = decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}*/
