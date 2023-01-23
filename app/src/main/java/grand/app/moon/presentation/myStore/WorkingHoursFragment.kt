package grand.app.moon.presentation.myStore

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentWorkingHoursBinding
import grand.app.moon.domain.shop.ResponseWorkingHour
import grand.app.moon.extensions.handleRetryAbleActionOrGoBackNullable
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.WorkingHoursViewModel

@AndroidEntryPoint
class WorkingHoursFragment : BaseFragment<FragmentWorkingHoursBinding>() {

	private val viewModel by viewModels<WorkingHoursViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBackNullable(
			action = {
				viewModel.repoShop.getWorkingHours()
			}
		) {
			val list = it.toItemWorkingHours2(context ?: return@handleRetryAbleActionOrGoBackNullable)

			viewModel.adapter.submitList(list)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_working_hours

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)
	}

}

fun List<ResponseWorkingHour>?.toItemWorkingHours2(context: Context): List<ItemWorkingHours2> = context.run {
	List(7) { index ->
		val response = this@toItemWorkingHours2?.getOrNull(index) ?: ResponseWorkingHour()

		val day = when (index) {
			0 -> getString(R.string.saturday)
			1 -> getString(R.string.sunday)
			2 -> getString(R.string.monday)
			3 -> getString(R.string.tuesday)
			4 -> getString(R.string.wednesday)
			5 -> getString(R.string.thursday)
			else -> getString(R.string.friday)
		}

		ItemWorkingHours2(
			day,
			response.from.orEmpty(),
			response.to.orEmpty(),
			response.enabled.orFalse()
		)
	}
}

data class ItemWorkingHours2(
	var day: String,
	var from: String,
	var to: String,
	var enabled: Boolean
)
