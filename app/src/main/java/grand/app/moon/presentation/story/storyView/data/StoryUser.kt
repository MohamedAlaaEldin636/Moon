package grand.app.moon.presentation.story.storyView.data

import android.os.Parcelable
import grand.app.moon.presentation.story.storyView.data.Story
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryUser(val username: String, val profilePicUrl: String, val stories: ArrayList<Story>) : Parcelable