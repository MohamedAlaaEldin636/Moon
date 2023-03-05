package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSingleOrMultiSelection2Binding
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setCompoundDrawablesRelativeWithIntrinsicBoundsEnd
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.ReportingDialogFragment
import grand.app.moon.presentation.home.ReportingDialogFragmentArgs
import grand.app.moon.presentation.home.models.ResponseReason
import javax.inject.Inject

@HiltViewModel
class ReportingViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: ReportingDialogFragmentArgs
) : AndroidViewModel(application) {

	private var selectedId: Int? = null

	val adapter = RVItemCommonListUsage<ItemSingleOrMultiSelection2Binding, ResponseReason>(
		R.layout.item_single_or_multi_selection_2,
		onItemClick = { adapter, binding ->
			val item = binding.root.getTagJson<ResponseReason>() ?: return@RVItemCommonListUsage

			val oldPosition = adapter.list.indexOfFirstOrNull { it.id == selectedId }
			val newPosition = adapter.list.indexOfFirstOrNull { it.id == item.id }

			selectedId = item.id

			adapter.notifyItemsChanged(oldPosition, newPosition)
		}
	) { binding, _, item ->
		binding.root.setTagJson(item)

		binding.textView.text = item.content
		binding.textView.setCompoundDrawablesRelativeWithIntrinsicBoundsEnd(
			if (item.id == selectedId) {
				R.drawable.ic_baseline_radio_button_checked_24
			}else {
				R.drawable.ic_baseline_radio_button_unchecked_24
			}
		)
	}

	fun goBack(view: View) {
		view.findFragmentOrNull<ReportingDialogFragment>()?.findNavController()?.navigateUp()
	}

	fun doNothing() {
		// Do nothing.
	}

	fun confirm(view: View) {
		val fragment = view.findFragmentOrNull<ReportingDialogFragment>() ?: return

		if (selectedId == null) {
			return fragment.showMessage(fragment.getString(R.string.perform_selection_firstly))
		}

		fragment.handleRetryAbleActionOrGoBackNullable(
			action = {
				when (args.type) {
					ReportingDialogFragment.Type.REPORT_ADS -> {
						repoShop.reportAdv(
							args.id,
							selectedId.orZero()
						)
					}
					ReportingDialogFragment.Type.REPORT_STORES -> {
						repoShop.reportStore(
							args.id,
							selectedId.orZero()
						)
					}
					ReportingDialogFragment.Type.BLOCK_STORES -> {
						repoShop.blockStore(
							args.id,
							selectedId.orZero()
						)
					}
				}
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			if (args.type == ReportingDialogFragment.Type.BLOCK_STORES) {
				fragment.setFragmentResultUsingJson(ReportingDialogFragment.Type.BLOCK_STORES.name, args.id)
			}
			fragment.findNavController().navigateUp()
		}
	}

}
