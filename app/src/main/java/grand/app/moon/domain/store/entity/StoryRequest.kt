package grand.app.moon.domain.store.entity

import com.google.gson.annotations.SerializedName

data class StoryRequest(@SerializedName("story_id")
                              var story_id: Int? = -1,
                        @SerializedName("type")
                        var type: Int? = -1) {

}