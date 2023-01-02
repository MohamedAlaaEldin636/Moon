package grand.app.moon.presentation.home.viewModels

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.presentation.home.AnnouncementDialogFragment
import grand.app.moon.presentation.home.AnnouncementDialogFragmentArgs
import grand.app.moon.presentation.home.AppGlobalAnnouncementDialogFragment
import grand.app.moon.presentation.home.AppGlobalAnnouncementDialogFragmentArgs
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
	args: AnnouncementDialogFragmentArgs
) : ViewModel() {

	val image = MutableLiveData(args.image)

	fun closeDialog(view: View) {
		val fragment = view.findFragment<AnnouncementDialogFragment>()
		fragment.findNavController().navigateUp()
	}

}
