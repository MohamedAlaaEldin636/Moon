package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.fromJsonInlinedOrNull
import grand.app.moon.presentation.myAds.adapter.RVSliderImageWrap
import grand.app.moon.presentation.myStore.ShowImagesOrVideoDialogFragment
import grand.app.moon.presentation.myStore.ShowImagesOrVideoDialogFragmentArgs
import javax.inject.Inject

@HiltViewModel
class ShowImagesOrVideoViewModel @Inject constructor(
	application: Application,
	val args: ShowImagesOrVideoDialogFragmentArgs,
) : AndroidViewModel(application) {

	val showImagesNotVideo = MutableLiveData(args.areImagesNotVideo)

	val files = args.jsonListOfUrls.fromJsonInlinedOrNull<List<String>>().orEmpty()

	val adapter = RVSliderImageWrap(files)

	fun close(view: View) {
		view.findFragmentOrNull<ShowImagesOrVideoDialogFragment>()?.findNavController()?.navigateUp()
	}

}
