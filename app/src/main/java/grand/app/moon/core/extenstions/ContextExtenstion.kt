package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import grand.app.moon.core.MyApplication
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.presentation.base.utils.Constants

fun Context.dpToPx(value: Float): Float {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
  )
}

/** - Layout inflater from `receiver`, by using [LayoutInflater.from] */
val Context.layoutInflater: LayoutInflater
  get() = LayoutInflater.from(this)

/**
 * - Inflates a layout from [layoutRes] isa.
 *
 * @param parent provide [ViewGroup.LayoutParams] to the returned root view, default is `null`
 * @param attachToRoot if true then the returned view will be attached to [parent] if not `null`,
 * default is false isa.
 *
 * @return rootView in the provided [layoutRes] isa.
 */
@JvmOverloads
fun Context.inflateLayout(
  @LayoutRes layoutRes: Int,
  parent: ViewGroup? = null,
  attachToRoot: Boolean = false
): View {
  return layoutInflater.inflate(layoutRes, parent, attachToRoot)
}

fun Context.getLanguage(): String {
  val appPreferences =  MyApplication.instance.getSharedPreferences(
    AppPreferences.APP_PREFERENCES_NAME,
    Context.MODE_PRIVATE
  )
  return appPreferences.getString(Constants.LANGUAGE,"ar").toString()
}
