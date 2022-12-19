package grand.app.moon.presentation.home.viewModels

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.presentation.home.AppGlobalAnnouncementDialogFragment
import grand.app.moon.presentation.home.AppGlobalAnnouncementDialogFragmentArgs
import javax.inject.Inject

@HiltViewModel
class AppGlobalAnnouncementViewModel @Inject constructor(
	args: AppGlobalAnnouncementDialogFragmentArgs
) : ViewModel() {

	val title = MutableLiveData(args.title)
	val description = MutableLiveData(args.description)
	val lottieLink = MutableLiveData(args.lottieLink)

	fun closeDialog(view: View) {
		val fragment = view.findFragment<AppGlobalAnnouncementDialogFragment>()
		fragment.findNavController().navigateUp()
	}

}
