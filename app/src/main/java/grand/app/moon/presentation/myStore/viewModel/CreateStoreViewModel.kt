package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateStoreViewModel @Inject constructor(
	application: Application
) : AndroidViewModel(application) {



}
