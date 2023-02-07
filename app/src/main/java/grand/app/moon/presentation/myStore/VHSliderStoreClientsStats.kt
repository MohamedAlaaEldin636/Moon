package grand.app.moon.presentation.myStore

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.smarteist.autoimageslider.SliderViewAdapter
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.core.extenstions.layoutInflater
import grand.app.moon.databinding.ItemSliderStatsChartBinding
import grand.app.moon.domain.stats.ChartData
import grand.app.moon.domain.stats.toChartData
import grand.app.moon.extensions.app
import grand.app.moon.extensions.orFalse
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
import grand.app.moon.presentation.stats.viewModels.ItemStatsChartViewModel

class RVSliderStoreClientsStats2 : SliderViewAdapter<VHSliderStoreClientsStats2>() {

	var size = 0

	override fun getCount(): Int = size

	override fun onCreateViewHolder(parent: ViewGroup?): VHSliderStoreClientsStats2 {
		return VHSliderStoreClientsStats2(parent!!)
	}

	override fun onBindViewHolder(viewHolder: VHSliderStoreClientsStats2?, position: Int) {}

	fun changeSize(size: Int) {
		this.size = size
		notifyDataSetChanged()
	}

}

class VHSliderStoreClientsStats2(
	parent: ViewGroup,
) : SliderViewAdapter.ViewHolder(
	parent.context.inflateLayout(R.layout.item_empty_frame_layout, parent)
)

class RVSliderStoreClientsStats(
	private val getLifecycleOwner: () -> LifecycleOwner,
) : SliderViewAdapter<VHSliderStoreClientsStats>() {

	private var list = emptyList<ResponseGeneralStats>()

	private val mapCurrentWeek = mutableMapOf<Int, Boolean>()

	override fun getCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup?): VHSliderStoreClientsStats {
		return VHSliderStoreClientsStats(parent!!, getLifecycleOwner)
	}

	override fun onBindViewHolder(viewHolder: VHSliderStoreClientsStats?, position: Int) {
		viewHolder?.bind(position, list[position], "", "", mapCurrentWeek[position].orFalse()) { index, newIsCurrentWeek ->
			mapCurrentWeek[index] = newIsCurrentWeek

			notifyDataSetChanged()
		}
	}

	fun submitList(list: List<ResponseGeneralStats>) {
		this.list = list
		mapCurrentWeek.clear()
		for ((index, item) in list.withIndex()) {
			mapCurrentWeek[index] = true
		}
		notifyDataSetChanged()
	}

}

class VHSliderStoreClientsStats(
	parent: ViewGroup,
	getLifecycleOwner: () -> LifecycleOwner
) : SliderViewAdapter.ViewHolder(
	parent.context.inflateLayout(R.layout.item_slider_stats_chart, parent)
) {

	private val binding = DataBindingUtil.inflate<ItemSliderStatsChartBinding>(
		parent.context.layoutInflater,
		R.layout.item_slider_stats_chart,
		parent,
		false
	)

	private val context get() = binding.root.context

	init {
		binding.lifecycleOwner = getLifecycleOwner()
	}

	fun bind(position: Int, item: ResponseGeneralStats, titlePlural: String, titleSingular: String, isCurrentWeek: Boolean, onToggleWeekAction: (Int, Boolean) -> Unit) {
		val appContext = context?.applicationContext as? Application ?: return

		val chartData = item.toChartData(appContext, titlePlural, titleSingular, isCurrentWeek)

		binding.viewModel = ViewModelSliderStoreClientsStats(
			appContext, item, titlePlural, titleSingular, chartData
		) { newIsCurrentWeek ->
			onToggleWeekAction(position, newIsCurrentWeek)
		}
	}

}

class ViewModelSliderStoreClientsStats(
	application: Application,
	private val response: ResponseGeneralStats,
	private val titlePlural: String,
	private val titleSingular: String,
	initialChartData: ChartData,
	private val onToggleWeekAction: (Boolean) -> Unit,
) : ItemStatsChartViewModel(application) {

	init {
		chart.postValue(initialChartData)
	}

	override fun toggleWeek(view: View) {
		val response = response
		val oldChart = chart.value ?: return
		val newIsCurrentWeek = oldChart.weekName != app.getString(R.string.current_week)
		onToggleWeekAction(newIsCurrentWeek)
		chart.value = response.toChartData(
			app,
			titlePlural,
			titleSingular,
			newIsCurrentWeek
		).copy(
			showSaturdayTooltip = oldChart.showSaturdayTooltip,
			showSundayTooltip = oldChart.showSundayTooltip,
			showMondayTooltip = oldChart.showMondayTooltip,
			showTuesdayTooltip = oldChart.showTuesdayTooltip,
			showWednesdayTooltip = oldChart.showWednesdayTooltip,
			showThursdayTooltip = oldChart.showThursdayTooltip,
			showFridayTooltip = oldChart.showFridayTooltip,
		)
	}
}
