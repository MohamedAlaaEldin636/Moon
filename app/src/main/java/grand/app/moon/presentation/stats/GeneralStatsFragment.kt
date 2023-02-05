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
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.stats.viewModels.GeneralStatsViewModel

/**
 * - Shows stats of 1 screen Ex. ->
 * [Link](https://xd.adobe.com/view/5896ecdd-111c-4559-a33c-e72b4c07f721-0dc5/screen/aca34b68-9668-4514-ba17-54424c266e00)
 */
@AndroidEntryPoint
class GeneralStatsFragment : BaseFragment<FragmentGeneralStatsBinding>() {

	private val viewModel by viewModels<GeneralStatsViewModel>()

	// todo from to name
	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getMyAdvStatsUsers(
					viewModel.args.advId,
					viewModel.args.type,
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

			viewModel.data.value = it.toChartData(context, viewModel.args, true)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_general_stats

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.statsInclude.bar1View // todo setup clicks ba2a isa. bars & current week bs kda isa.
	}

}
