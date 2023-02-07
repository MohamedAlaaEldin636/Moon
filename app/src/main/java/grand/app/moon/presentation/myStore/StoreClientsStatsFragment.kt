package grand.app.moon.presentation.myStore

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import coil.load
import coil.request.videoFrameMillis
import coil.request.videoFramePercent
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentStoreClientsStatsBinding
import grand.app.moon.databinding.ItemSliderStatsChartBinding
import grand.app.moon.domain.stats.ChartData
import grand.app.moon.domain.stats.toChartData
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.StoreClientsStatsViewModel
import grand.app.moon.presentation.stats.models.ItemStoreStats
import grand.app.moon.presentation.stats.viewModels.ItemStatsChartViewModel
import okhttp3.HttpUrl.Companion.toHttpUrl

@AndroidEntryPoint
class StoreClientsStatsFragment : BaseFragment<FragmentStoreClientsStatsBinding>() {

	private val viewModel by viewModels<StoreClientsStatsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_store_clients_stats

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// todo in more or any other page with web view if press on link redirection to other than this then just onBackPressed
		// todo in explore create only 1 player 34an 3amel lagg gamed isa.
		binding.viewPager2.adapter = viewModel.adapterViewPager
		binding.sliderView.setSliderAdapter(viewModel.adapterSliderView)
		binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				if (binding.sliderView.currentPagePosition != position) {
					binding.sliderView.currentPagePosition = position
					updateChart(position)
				}
			}
		})
		binding.sliderView.setCurrentPageListener {
			if (binding.viewPager2.currentItem != it) {
				binding.viewPager2.currentItem = it
				updateChart(it)
			}
		}

		binding.recyclerViewStats.setupWithRVItemCommonListUsage(
			viewModel.adapterStats,
			false,
			2
		)

		binding.recyclerViewLatestSpecialAds.setupWithRVItemCommonListUsage(
			viewModel.adapterLatestSpecialAds,
			false,
			1
		)
		binding.recyclerViewHighestViewedAds.setupWithRVItemCommonListUsage(
			viewModel.adapterHighestViewedAds,
			false,
			1
		)
		binding.recyclerViewRecentlyAddedAds.setupWithRVItemCommonListUsage(
			viewModel.adapterRecentlyAddedAds,
			false,
			1
		)

		viewModel.search(binding.searchButton)
	}

	fun updateChart(index: Int) {
		val application = context?.applicationContext as? Application ?: return

		val item = viewModel.adapterViewPager.list.getOrNull(index) ?: return

		val type = item.type ?: return
		val chartData = map[item.type] ?: item.chart.toChartData(
			viewModel.app,
			type.titlePluralRes.let { viewModel.app.getString(it) },
			type.titleSingularRes.let { viewModel.app.getString(it) },
			true
		)

		val viewHolder = (binding.viewPager2.getOrNull(0) as? RecyclerView)?.findViewHolderForAdapterPosition(index)
		if (viewHolder is VHItemCommonListUsage<*, *>) {
			val binding = viewHolder.binding

			if (binding is ItemSliderStatsChartBinding) {
				binding.includeItemStatsChart.viewModel = ChartViewPagerViewModel(application, chartData, item) {
					item.type?.also { type ->
						map[type] = it
					}
				}
				binding.includeItemStatsChart.lifecycleOwner = this
				binding.includeItemStatsChart.executePendingBindings()
			}
		}
	}

	private val map = mutableMapOf<ItemStoreStats.Type, ChartData>()

}

class ChartViewPagerViewModel(
	application: Application,
	initialChartData: ChartData,
	private val item: ItemStoreStats,
	private val onAnyChange: (ChartData) -> Unit
) : ItemStatsChartViewModel(application) {

	override val initialChart: ChartData = initialChartData

	override fun toggleWeek(view: View) {
		val oldChart = chart.value ?: return
		val newIsCurrentWeek = oldChart.weekName != app.getString(R.string.current_week)
		chart.value = item.chart.toChartData(
			app,
			item.type?.titlePluralRes?.let { app.getString(it) }.orEmpty(),
			item.type?.titleSingularRes?.let { app.getString(it) }.orEmpty(),
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

		onAnyChange(chart.value ?: return)
	}

	override fun showDayTooltip(view: View) {
		super.showDayTooltip(view)

		onAnyChange(chart.value ?: return)
	}

}
