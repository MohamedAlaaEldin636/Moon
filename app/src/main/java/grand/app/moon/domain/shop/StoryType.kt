package grand.app.moon.domain.shop

import androidx.annotation.StringRes
import grand.app.moon.R

enum class StoryType(val apiValue: Int, @StringRes val stringRes: Int) {
	STORY(0, R.string.non_highlighted_story),
	HIGHLIGHT(1, R.string.highlighted_story)
}

//1=>whatsapp,2=>call,3=>chat
enum class StoryLink(val apiValue: Int, @StringRes val stringRes: Int) {
	WHATSAPP(1, R.string.whatsapp_2),
	CALL(2, R.string.call_2),
	CHAT(3, R.string.chat_2),
}
