package grand.app.moon.presentation.story.storyView.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(val url: String, val storyDate: Long) : Parcelable {

    fun isVideo() =  url.contains(".mp4")
}