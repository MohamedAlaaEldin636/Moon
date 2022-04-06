package grand.app.moon.helpers.map

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

class MarkerHelper : Parcelable {
  var text_select: String? = ""
  var latLng: LatLng?

  constructor(text_select: String?, latLng: LatLng?) {
    this.text_select = text_select
    this.latLng = latLng
  }

  protected constructor(`in`: Parcel) {
    text_select = `in`.readString()
    latLng = `in`.readParcelable(LatLng::class.java.classLoader)
  }

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(text_select)
    dest.writeParcelable(latLng, flags)
  }

  companion object CREATOR : Parcelable.Creator<MarkerHelper> {
    override fun createFromParcel(parcel: Parcel): MarkerHelper {
      return MarkerHelper(parcel)
    }

    override fun newArray(size: Int): Array<MarkerHelper?> {
      return arrayOfNulls(size)
    }
  }
}