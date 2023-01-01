package grand.app.moon.presentation.myAds.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAdvertisementViewModel @Inject constructor() : ViewModel() {

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

}
