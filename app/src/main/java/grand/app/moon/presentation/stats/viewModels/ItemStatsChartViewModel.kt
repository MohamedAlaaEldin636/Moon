package grand.app.moon.presentation.stats.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import grand.app.moon.R
import grand.app.moon.domain.stats.ChartData
import java.time.DayOfWeek

abstract class ItemStatsChartViewModel(
	application: Application,
) : AndroidViewModel(application) {

	open val initialChart = ChartData()

	val chart by lazy {
		MutableLiveData(initialChart)
	}

	abstract fun toggleWeek(view: View)

	open fun showDayTooltip(view: View) {
		val chart = chart.value ?: return

		val day = when (view.id) {
			R.id.bar1View -> DayOfWeek.SATURDAY
			R.id.bar2View -> DayOfWeek.SUNDAY
			R.id.bar3View -> DayOfWeek.MONDAY
			R.id.bar4View -> DayOfWeek.TUESDAY
			R.id.bar5View -> DayOfWeek.WEDNESDAY
			R.id.bar6View -> DayOfWeek.THURSDAY
			R.id.bar7View -> DayOfWeek.FRIDAY
			else -> return
		}

		this.chart.value = chart.performShowTooltip(day)
	}

}
