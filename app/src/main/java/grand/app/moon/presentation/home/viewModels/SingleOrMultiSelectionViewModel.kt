package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.databinding.ItemSingleOrMultiSelectionBinding
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.SingleOrMultiSelectionFragment
import grand.app.moon.presentation.home.SingleOrMultiSelectionFragmentArgs
import javax.inject.Inject

@HiltViewModel
class SingleOrMultiSelectionViewModel @Inject constructor(
	application: Application,
	val args: SingleOrMultiSelectionFragmentArgs,
) : AndroidViewModel(application) {

	private val list = args.jsonOfIdAndNameList.fromJsonInlinedOrNull<List<IdAndName>>().orEmpty()

	private val initialSelection = args.jsonOfCurrentlySelectedIdAndNameList.fromJsonInlinedOrNull<List<IdAndName>>().orEmpty()

	/** Can have only 1 item in selection if single mode selection */
	private val selectedItems = mutableListOf(*initialSelection.toTypedArray())

	val adapter = RVItemCommonListUsage<ItemSingleOrMultiSelectionBinding, IdAndName>(
		R.layout.item_single_or_multi_selection,
		list,
		onItemClick = { adapter, binding ->
			val position = binding.constraintLayout.tag as? Int ?: return@RVItemCommonListUsage

			val item = list[position]

			val isCurrentlySelected = selectedItems.any { it.id == item.id }

			if (args.isSingleNotMultiSelection) {
				if (isCurrentlySelected.not()) {
					val previousSelectedItemPosition = selectedItems.firstOrNull()?.let { selectedItem ->
						list.indexOfFirstOrNull { selectedItem.id == it.id }
					}

					selectedItems.clear()
					selectedItems += item

					adapter.notifyItemsChanged(previousSelectedItemPosition, position)
				}// else do Nothing.
			}else {
				if (isCurrentlySelected) {
					selectedItems.remove(item)
				}else {
					selectedItems.add(item)
				}

				adapter.notifyItemChanged(position)
			}
		}
	) { binding, position, item ->
		binding.constraintLayout.tag = position

		val isSelected = selectedItems.any { it.id == item.id }

		val iconRes = if (args.isSingleNotMultiSelection) {
			if (isSelected) R.drawable.ic_baseline_radio_button_checked_24 else R.drawable.ic_baseline_radio_button_unchecked_24
		}else {
			if (isSelected) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24
		}

		binding.textView.text = item.name
		binding.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
			0,
			0,
			iconRes,
			0
		)
	}

	fun confirm(view: View) {
		val fragment = view.findFragmentOrNull<SingleOrMultiSelectionFragment>() ?: return

		if (selectedItems.isEmpty()) {
			return fragment.showMessage(fragment.getString(R.string.perform_selection_firstly))
		}

		fragment.findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
			if (args.isSingleNotMultiSelection) selectedItems.first() else selectedItems,
			if (args.isSingleNotMultiSelection) {
				SingleOrMultiSelectionFragment.KEY_RESULT_SINGLE_SELECTION
			}else {
				SingleOrMultiSelectionFragment.KEY_RESULT_MULTI_SELECTION
			}
		)
	}

}
