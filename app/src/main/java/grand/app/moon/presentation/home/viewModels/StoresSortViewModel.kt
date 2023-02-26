package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.setFragmentResultUsingJson
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.AllStoresFragment
import grand.app.moon.presentation.home.StoresSortDialogFragment
import grand.app.moon.presentation.home.StoresSortDialogFragmentArgs
import javax.inject.Inject

@HiltViewModel
class StoresSortViewModel @Inject constructor(
	application: Application,
	val args: StoresSortDialogFragmentArgs
) : AndroidViewModel(application) {

	val selectedSortBy = MutableLiveData(
		AllStoresFragment.SortBy.values().firstOrNull { it.name == args.nameOfSortBy }
	)

	fun changeSelectedSortBy(sortBy: AllStoresFragment.SortBy) {
		selectedSortBy.value = sortBy
	}

	fun confirmSelection(view: View) {
		val fragment = view.findFragmentOrNull<StoresSortDialogFragment>() ?: return

		if (selectedSortBy.value == null) {
			return fragment.showMessage(fragment.getString(R.string.perform_selection_firstly))
		}

		fragment.setFragmentResultUsingJson(
			StoresSortDialogFragment::class.java.name,
			selectedSortBy.value
		)

		fragment.findNavController().navigateUp()
	}

}
