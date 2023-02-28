package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllAdsOfCategoryViewModel @Inject constructor(
	application: Application,
) : AndroidViewModel(application) {



}
