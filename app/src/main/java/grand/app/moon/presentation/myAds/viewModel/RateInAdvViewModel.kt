package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateInAdvViewModel @Inject constructor(
	application: Application,
) : AndroidViewModel(application) {



}
