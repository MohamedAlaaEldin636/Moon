package com.structure.base_mvvm.domain.utils

import androidx.annotation.Keep
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Keep
open class BaseRequest {
  @Transient
  var image: ArrayList<MultipartBody.Part> = arrayListOf()
  fun setImage(path: String, key: String){
    val file = File(path)
    val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
    image.add(MultipartBody.Part.createFormData(key, file.name, requestFile))
  }
}