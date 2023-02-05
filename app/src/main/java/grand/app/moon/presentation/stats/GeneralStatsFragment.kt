package grand.app.moon.presentation.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentGeneralStatsBinding
import grand.app.moon.domain.stats.toChartData
import grand.app.moon.extensions.RetryAbleFlow
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.minLengthZerosPrefix
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.stats.viewModels.GeneralStatsViewModel

/**
 * - Shows stats of 1 screen Ex. ->
 * [Link](https://xd.adobe.com/view/5896ecdd-111c-4559-a33c-e72b4c07f721-0dc5/screen/aca34b68-9668-4514-ba17-54424c266e00)
 */
@AndroidEntryPoint
class GeneralStatsFragment : BaseFragment<FragmentGeneralStatsBinding>() {

	private val viewModel by viewModels<GeneralStatsViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getMyAdvStatsUsers(
					viewModel.args.advId,
					viewModel.args.type,
					viewModel.name.value,
					viewModel.dateFrom.value?.fromUiToApiDate(),
					viewModel.dateTo.value?.fromUiToApiDate(),
				)
			},
			collectLatestAction = {
				viewModel.adapter.submitData(it)
			},
			onRetry = {
				viewModel.adapter.refresh()
			}
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getMyAdvStats(
					viewModel.args.advId,
					viewModel.args.type,
				)
			}
		) {
			val context = context ?: return@handleRetryAbleActionOrGoBack

			viewModel.response = it

			viewModel.chart.value = it.toChartData(context, viewModel.args, true)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_general_stats

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter.withDefaultHeaderAndFooterAdapters(),
			false,
			1
		)

		retryAbleFlow.collectLatest()
	}

	private fun String.fromUiToApiDate(): String? {
		val array = if (contains(" / ")) split(" / ") else return null

		val day = array.getOrNull(0)?.toIntOrNull() ?: return null
		val month = array.getOrNull(1)?.toIntOrNull() ?: return null
		val year = array.getOrNull(2)?.toIntOrNull() ?: return null

		return "${year.toString().minLengthZerosPrefix(4)}-" +
			"${month.toString().minLengthZerosPrefix(2)}-" +
			day.toString().minLengthZerosPrefix(2)
	}

}
