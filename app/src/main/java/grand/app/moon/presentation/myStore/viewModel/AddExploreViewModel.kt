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
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.AddExploreFragment
import javax.inject.Inject

@HiltViewModel
class AddExploreViewModel @Inject constructor(
	application: Application,
	private val repoShop: RepoShop
) : AndroidViewModel(application) {

	val uris = MutableLiveData<MAImagesOrVideo?>()

	val showVideoIndicator = uris.map {
		it is MAImagesOrVideo.Video
	}

	/*val showMultiImageIndicator = uris.map {
		it is MAImagesOrVideo.Images && it.images.size > 1
	}*/

	fun pickImagesOrVideo(view: View) {
		val fragment = view.findFragmentOrNull<AddExploreFragment>() ?: return

		MyLogger.e("aaaaaaaaaa -> Clickedddddddd ${fragment.gettingImagesOrVideoHandler}")

		fragment.gettingImagesOrVideoHandler?.requestImageOrVideo()
	}

	fun add(view: View) {
		if (uris.value == null) return pickImagesOrVideo(view)

		val fragment = view.findFragmentOrNull<AddExploreFragment>() ?: return

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.addExplore(
					uris.value?.getUris().orEmpty().mapNotNull {
						it.createMultipartBodyPart(app, "file[]")
					}
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
