package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.extensions.app
import grand.app.moon.presentation.home.AllStoresFragment
import grand.app.moon.presentation.home.CategoryDetails2FragmentArgs
import grand.app.moon.presentation.map.MapOfDataFragment
import javax.inject.Inject

@HiltViewModel
class CategoryDetails2ViewModel @Inject constructor(
	application: Application,
	val args: CategoryDetails2FragmentArgs
) : AndroidViewModel(application) {

	val searchQuery = MutableLiveData("")

	val storesLabelText by lazy {
		"${app.getString(R.string.stores)} ${args.categoryName}"
	}

	fun goToMap(view: View) {
		MapOfDataFragment.goToThisScreenForStores(view.findNavController())
	}

	fun showAllStores(view: View) {
		AllStoresFragment.launch(
			view.findNavController(),
			AllStoresFragment.Filter(categoryId = args.categoryId),
			app.getString(R.string.stores_879)
		)
	}

}
