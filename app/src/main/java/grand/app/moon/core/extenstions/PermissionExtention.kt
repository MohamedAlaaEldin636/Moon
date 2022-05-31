package com.maproductions.mohamedalaa.shared.core.extensions

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.presentation.base.extensions.showPopup
import grand.app.moon.presentation.base.utils.Constants
import java.io.ByteArrayOutputStream
import java.util.*
fun Fragment.pickImage(fromCamera: Boolean) {

  if (fromCamera) {
//    activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
  }else {
    // From gallery
//    activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
  }
}