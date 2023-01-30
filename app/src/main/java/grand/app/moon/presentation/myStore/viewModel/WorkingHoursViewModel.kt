package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.content.Context
import android.view.View
import androidx.core.view.postDelayed
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemWorkingHours2Binding
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.adjustInsideRV
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import grand.app.moon.presentation.myStore.WorkingHoursFragment
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkingHoursViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val adapter = RVItemCommonListUsage<ItemWorkingHours2Binding, ItemWorkingHours2>(
		R.layout.item_working_hours_2,
		additionalListenersSetups = { adapter, binding ->
			binding.fromTextView.setOnClickListener { view ->
				val position = binding.fromTextView.tag as? Int ?: return@setOnClickListener

				view.showTimePicker(
					adapter.list[position].from
				) { time ->
					adapter.list[position].also {
						it.from = time
					}

					adapter.notifyItemChanged(position)
				}
			}

			binding.toTextView.setOnClickListener { view ->
				val position = binding.fromTextView.tag as? Int ?: return@setOnClickListener

				view.showTimePicker(
					adapter.list[position].to
				) { time ->
					adapter.list[position].also {
						it.to = time
					}

					adapter.notifyItemChanged(position)
				}
			}

			binding.imageView.setOnClickListener {
				val position = binding.fromTextView.tag as? Int ?: return@setOnClickListener

				adapter.list[position].enabled = adapter.list[position].enabled.not()

				adapter.notifyItemChanged(position)
			}
		}
	) { binding, position, item ->
		val context = binding.root.context

		binding.textView.tag = item.toJsonInlinedOrNull()
		binding.fromTextView.tag = position

		binding.textView.text = item.day

		binding.fromTextView.adjustInsideRV(
			item.from.toAppTimeFormat(context),
			10.5f,
			6f
		)

		binding.toTextView.adjustInsideRV(
			item.to.toAppTimeFormat(context),
			10.5f,
			6f
		)

		binding.imageView.setImageResource(
			if (item.enabled) R.drawable.ic_switch_on_1 else R.drawable.ic_switch_off_2
		)
	}

	private fun String?.toAppTimeFormat(context: Context): String {
		val hour = orEmpty().split(":").getOrNull(0)
			?.toIntOrNull() ?: return ""
		val minute = orEmpty().split(":").getOrNull(1)
			?.toIntOrNull() ?: return ""

		val (hour12, isAm) = when {
			hour == 0 -> 12 to true
			hour < 12 -> hour to true
			hour == 12 -> 12 to false
			else -> (hour - 12) to false
		}

		return if (isAm) {
			context.getString(R.string.working_hours_value_am, hour12, minute)
		}else {
			context.getString(R.string.working_hours_value_pm, hour12, minute)
		}
	}

	/**
	 * @param currentTimeInApiFormat format Ex. 21:20
	 */
	private fun View.showTimePicker(
		currentTimeInApiFormat: String?,
		onChange: (timeInAPiFormat: String) -> Unit
	) {
		val fragment = findFragmentOrNull<WorkingHoursFragment>() ?: return

		val now = LocalDateTime.now()

		val hour = currentTimeInApiFormat.orEmpty().split(":").getOrNull(0)
			?.toIntOrNull() ?: now.hour
		val minute = currentTimeInApiFormat.orEmpty().split(":").getOrNull(1)
			?.toIntOrNull() ?: now.minute

		val picker = MaterialTimePicker.Builder()
			.setTimeFormat(TimeFormat.CLOCK_12H)
			.setHour(hour)
			.setMinute(minute)
			.setTitleText(fragment.getString(R.string.select_time))
			//.setTheme(R.style.MyStyle)
			.build()

		picker.addOnPositiveButtonClickListener {
			onChange("${picker.hour.toString().minLengthZerosPrefix(2)}:${picker.minute.toString().minLengthZerosPrefix(2)}")
		}

		picker.show(fragment.childFragmentManager, "Time")
	}

	fun save(view: View) {
		val fragment = view.findFragmentOrNull<WorkingHoursFragment>() ?: return

		if (adapter.list.any { it.enabled && (it.from.isEmpty() || it.to.isEmpty()) }) {
			return fragment.showError(fragment.getString(R.string.invalid_working_hours_hint))
		}

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.saveWorkingHours(adapter.list)
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navigateUp()
		}
	}

}
