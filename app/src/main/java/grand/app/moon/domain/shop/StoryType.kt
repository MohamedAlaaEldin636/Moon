package grand.app.moon.domain.shop

import androidx.annotation.StringRes
import grand.app.moon.R

enum class StoryType(val apiValue: Int, @StringRes val stringRes: Int) {
	STORY(0, R.string.non_highlighted_story),
	HIGHLIGHT(1, R.string.highlighted_story)
}
