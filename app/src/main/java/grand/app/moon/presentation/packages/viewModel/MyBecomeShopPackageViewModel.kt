package grand.app.moon.presentation.packages.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.extensions.General
import javax.inject.Inject

@HiltViewModel
class MyBecomeShopPackageViewModel @Inject constructor(
	application: Application,
) : AndroidViewModel(application) {

	fun renewOrSubscribeToNewPackage(view: View) {
		General.TODO("Will be programmed later since this page waiting for BACKEND work")
	}

}
