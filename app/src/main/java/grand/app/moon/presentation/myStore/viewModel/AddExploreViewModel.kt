package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.presentation.myStore.AddExploreFragment
import javax.inject.Inject

@HiltViewModel
class AddExploreViewModel @Inject constructor(
	application: Application,
) : AndroidViewModel(application) {

	val uris = MutableLiveData<MAImagesOrVideo?>()

	fun pickImagesOrVideo(view: View) {
		val fragment = view.findFragmentOrNull<AddExploreFragment>() ?: return

		fragment.gettingImagesOrVideoHandler?.requestImageOrVideo()
	}

	fun add(view: View) {
		if (uris.value == null) return pickImagesOrVideo(view)

		TODO()
	}

}
