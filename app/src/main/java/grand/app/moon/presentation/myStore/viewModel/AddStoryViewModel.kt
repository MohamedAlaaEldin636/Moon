package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.grand.trim_video_lib.launchSafelyTrimVideo
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.core.extenstions.getVideoLength
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.core.extenstions.ssssssssssss
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.domain.shop.StoryType
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.AddStoryFragment
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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

	val showVideoIndicator = file.map {
		it is MAImagesOrVideo.Video
	}

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

		if (file.value == null || link.value == null || type.value == null
			|| (type.value == StoryType.HIGHLIGHT && coverImage.value == null)
			|| (type.value == StoryType.HIGHLIGHT && name.value.isNullOrEmpty())) {
			return fragment.showError(fragment.getString(R.string.all_fields_required))
		}

		val fileUri = file.value?.getUris()?.firstOrNull() ?: return

		if (false && this.file.value is MAImagesOrVideo.Video) {
			/*
Intent intent = new Intent(activity, ActVideoTrimmer.class);
		Gson gson = new Gson();
		Bundle bundle = new Bundle();
		bundle.putString("trim_video_uri", this.videoUri);
		bundle.putString("trim_video_option", gson.toJson(this.options));
		intent.putExtras(bundle);
		return intent;
 */
			val length = fileUri.getVideoLength(fragment.requireContext())

			MyLogger.e("lennnnnnnnnn $length")

			Log.e("aa", "dofkpsdofk 1 $fileUri")

			fragment.launcherTrimVideo3.launch(fileUri/*.ssssssssssss(fragment.requireContext()).also {
				Log.e("aa", "dofkpsdofk 1 $it")
			}*/)
			/*fragment.launchSafelyTrimVideo(
				fileUri,
				fragment.launcherVideoTrimmer,
			) {
				getMAGson().toJson(it)
			}*/
		}else {
			MyLogger.e("djioejdowedo $fileUri")

			addStoryImmediately(fragment, fileUri.createMultipartBodyPart(app, "file") ?: return)
		}
	}

	fun addStoryImmediately(fragment: AddStoryFragment, file: MultipartBody.Part, makeHugeChanges: Boolean = false) {
		if (false && makeHugeChanges) {
			fragment.activity?.lifecycleScope?.launch {
				MyLogger.e("cccccccccccc -> 1")

				val resource = repoShop.addStory(
					file,
					link.value!!,
					type.value!!,
					name.value.orEmpty(),
					if (type.value != StoryType.HIGHLIGHT) null else {
						coverImage.value?.createMultipartBodyPart(app, "highlight_cover")
					},
				)

				MyLogger.e("cccccccccccc -> 2  ${resource is Resource.Success} $resource")
			}
		}else {
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

}
