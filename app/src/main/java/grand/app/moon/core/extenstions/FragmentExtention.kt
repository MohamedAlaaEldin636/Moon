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

fun Fragment.rateApp(){
  val uri = Uri.parse("market://details?id=${requireActivity().packageName}")
  val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
  try {
    startActivity(myAppLinkToMarket)
  } catch (e: ActivityNotFoundException) {
    Toast.makeText(requireContext(), "Impossible to find an application for the market", Toast.LENGTH_LONG).show()
  }
}
