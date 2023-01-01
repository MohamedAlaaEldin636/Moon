package grand.app.moon.presentation.myAds.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentArgs
import javax.inject.Inject

@HiltViewModel
class AddAdvFinalPageViewModel @Inject constructor(
	//private val homeUseCase: HomeUseCase,
	private val args: AddAdvFinalPageFragmentArgs
) : ViewModel() {

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}


}
