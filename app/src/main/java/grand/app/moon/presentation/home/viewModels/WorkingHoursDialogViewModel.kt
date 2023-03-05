package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreDetailsWorkingHoursBinding
import grand.app.moon.domain.shop.ResponseWorkingHour
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.WorkingHoursDialogFragment
import grand.app.moon.presentation.home.WorkingHoursDialogFragmentArgs
import javax.inject.Inject

@HiltViewModel
class WorkingHoursDialogViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: WorkingHoursDialogFragmentArgs
) : AndroidViewModel(application) {

	private val workingHours = args.jsonOfWorkingHoursList
		.fromJsonInlinedOrNull<List<ResponseWorkingHour>>().orEmpty().let {
			it.ifEmpty { List(7) { ResponseWorkingHour(enabled = false) } }
		}

	val adapter = RVItemCommonListUsage<ItemStoreDetailsWorkingHoursBinding, ResponseWorkingHour>(
		R.layout.item_store_details_working_hours,
		workingHours,
	) { binding, position, item ->
		binding.closedTextView.isVisible = item.enabled != true
		binding.timesTextView.isVisible = item.enabled == true
		binding.timesTextView.text = app.getString(R.string.from_var_am_to_var_pm, item.from.orEmpty(), item.to.orEmpty())
		binding.dayTextView.text = when (position) {
			0 -> getString(R.string.saturday)
			1 -> getString(R.string.sunday)
			2 -> getString(R.string.monday)
			3 -> getString(R.string.tuesday)
			4 -> getString(R.string.wednesday)
			5 -> getString(R.string.thursday)
			else -> getString(R.string.friday)
		}
	}

	fun goBack(view: View) {
		view.findFragmentOrNull<WorkingHoursDialogFragment>()?.findNavController()?.navigateUp()
	}

	fun doNothing() {
		// Do nothing.
	}

}
