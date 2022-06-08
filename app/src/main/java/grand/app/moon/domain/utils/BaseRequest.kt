package grand.app.moon.domain.utils

import androidx.annotation.Keep
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable

@Keep
open class BaseRequest : Serializable{
  @Transient
  var image: ArrayList<MultipartBody.Part> = arrayListOf()
  fun setImage(path: String, key: String){
    image.clear()
    val file = File(path)
    val requestFile: RequestBody = RequestBody.create(
      "multipart/form-data".toMediaTypeOrNull(),
      file
    )
    image.add(MultipartBody.Part.createFormData(key, file.name, requestFile))
  }
}