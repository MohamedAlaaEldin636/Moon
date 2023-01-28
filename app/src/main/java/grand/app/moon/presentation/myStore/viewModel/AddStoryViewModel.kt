package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.domain.shop.StoryType
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.AddStoryFragment
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
	application: Application,
	private val repoShop: RepoShop
) : AndroidViewModel(application) {

	val link = MutableLiveData<StoryLink?>()

	val textLink = link.map { link ->
		link?.let { app.getString(it.stringRes) }.orEmpty()
	}

	val type = MutableLiveData<StoryType?>()

	val textType = type.map { type ->
		type?.let { app.getString(it.stringRes) }.orEmpty()
	}

	val showCover = type.map {
		it == StoryType.HIGHLIGHT
	}

	val name = MutableLiveData("")

	val coverImage = MutableLiveData<Uri?>()

	val textCover = coverImage.map { uri ->
		uri?.let { app.getString(R.string.image_has_been_added) }.orEmpty()
	}

	val file = MutableLiveData<MAImagesOrVideo?>()

	fun pickStoryLinkingType(view: View) {
		view.showPopup(
			StoryLink.values().map { app.getString(it.stringRes) },
			listener = { menuItem ->
				link.value = StoryLink.values().firstOrNull { app.getString(it.stringRes) == menuItem.title.toStringOrEmpty() }
			}
		)
	}

	fun pickStoryType(view: View) {
		view.showPopup(
			StoryType.values().map { app.getString(it.stringRes) },
			listener = { menuItem ->
				type.value = StoryType.values().firstOrNull { app.getString(it.stringRes) == menuItem.title.toStringOrEmpty() }
			}
		)
	}

	fun pickCoverImage(view: View) {
		val fragment = view.findFragmentOrNull<AddStoryFragment>() ?: return

		fragment.gettingImageHandler?.requestImageOrVideo()
	}

	fun pickImagesOrVideo(view: View) {
		val fragment = view.findFragmentOrNull<AddStoryFragment>() ?: return

		fragment.gettingImagesOrVideoHandler?.requestImageOrVideo()
	}

	fun add(view: View) {
		val fragment = view.findFragmentOrNull<AddStoryFragment>() ?: return

		if (file.value == null || link.value == null || type.value == null || name.value.isNullOrEmpty()
			|| (type.value == StoryType.HIGHLIGHT && coverImage.value == null)) {
			return fragment.showError(fragment.getString(R.string.all_fields_required))
		}

		val file = file.value?.getUris()?.firstOrNull()?.createMultipartBodyPart(app, "file") ?: return

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.addStory(
					file,
					link.value!!,
					type.value!!,
					name.value.orEmpty(),
					if (type.value != StoryType.HIGHLIGHT) null else {
						coverImage.value?.createMultipartBodyPart(app, "highlight_cover")
				  },
				)
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
				true // Done successfully
			)
		}
	}

}
