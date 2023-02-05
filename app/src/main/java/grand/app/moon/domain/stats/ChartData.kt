package grand.app.moon.domain.stats

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import grand.app.moon.R
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.presentation.stats.GeneralStatsFragmentArgs
import grand.app.moon.presentation.stats.models.ResponseGeneralStats
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.DayOfWeek
import kotlin.math.roundToInt

fun ResponseGeneralStats.toChartData(
	context: Context,
	args: GeneralStatsFragmentArgs,
	isCurrentWeek: Boolean,
): ChartData {
	val percentage = if (isCurrentWeek) growthRateCurrentWeek else growthRatePreviousWeek

	val week = (if (isCurrentWeek) currentWeek else previousWeek).orEmpty()
	val maxWeekLineValue = week.maxOf { it }.coerceAtLeast(6) // 7 lines where line 1 is always 0 isa.
	MyLogger.e("toChartData -> maxWeekLineValue -> $maxWeekLineValue")

	val line1 = 0
	val step = ((maxWeekLineValue.toFloat() - line1.toFloat()) / 6f).roundToInt()
	val line2 = line1 + step
	val line3 = line2 + step
	val line4 = line3 + step
	val line5 = line4 + step
	val line6 = line5 + step
	val line7 = line6 + step
	MyLogger.e("toChartData -> lines -> $line7 $line6 $line5 $line4 $line3 $line2 $line1 ==== step -> $step")

	val saturdayPercent = week.getOrNull(0).orZero().toFloat() / line7.toFloat() * 100f
	MyLogger.e("toChartData -> saturdayPercent -> ${week.getOrNull(0)} ${line7.toFloat()} $saturdayPercent")
	val sundayPercent = week.getOrNull(1).orZero().toFloat() / line7.toFloat() * 100f
	val mondayPercent = week.getOrNull(2).orZero().toFloat() / line7.toFloat() * 100f
	val tuesdayPercent = week.getOrNull(3).orZero().toFloat() / line7.toFloat() * 100f
	val wednesdayPercent = week.getOrNull(4).orZero().toFloat() / line7.toFloat() * 100f
	val thursdayPercent = week.getOrNull(5).orZero().toFloat() / line7.toFloat() * 100f
	val fridayPercent = week.getOrNull(6).orZero().toFloat() / line7.toFloat() * 100f

	return ChartData(
		args.titlePlural,
		if (isCurrentWeek) context.getString(R.string.current_week) else context.getString(R.string.prev_week),
		percentage.toString(),
		if (percentage >= 0.toBigDecimal()) R.drawable.ic_positive_growth else R.drawable.ic_negative_growth,
		args.titleSingular,
		line7, line6, line5, line4, line3, line2, line1,
		saturdayPercent, sundayPercent, mondayPercent, tuesdayPercent, wednesdayPercent, thursdayPercent, fridayPercent
	)
}

/**
 * @param percentage scale is 2 decimal places only.
 * @param saturdayPercent from 0.0 to 1.0 where 0 is zero [line1] and 1.0 represents max value [line7] isa.
 * @param percentageIconRes [R.drawable.ic_positive_growth] || [R.drawable.ic_negative_growth]
 */
data class ChartData(
	val dataName: String = "",
	val weekName: String = "",
	val percentage: String = "",
	@DrawableRes val percentageIconRes: Int = R.drawable.ic_positive_growth,

	val tooltipSuffix: String = "",

	val line7: Int = 0,
	val line6: Int = 0,
	val line5: Int = 0,
	val line4: Int = 0,
	val line3: Int = 0,
	val line2: Int = 0,
	val line1: Int = 0,

	@FloatRange(from = 0.0, to = 1.0) val saturdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 1.0) val sundayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 1.0) val mondayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 1.0) val tuesdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 1.0) val wednesdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 1.0) val thursdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 1.0) val fridayPercent: Float = 0f,

	val showSaturdayTooltip: Boolean = false,
	val showSundayTooltip: Boolean = false,
	val showMondayTooltip: Boolean = false,
	val showTuesdayTooltip: Boolean = false,
	val showWednesdayTooltip: Boolean = false,
	val showThursdayTooltip: Boolean = false,
	val showFridayTooltip: Boolean = false,
) {

	val saturdayTooltip get() = "$saturdayPercent $tooltipSuffix"
	val sundayTooltip get() = "$saturdayPercent $tooltipSuffix"
	val mondayTooltip get() = "$saturdayPercent $tooltipSuffix"
	val tuesdayTooltip get() = "$saturdayPercent $tooltipSuffix"
	val wednesdayTooltip get() = "$saturdayPercent $tooltipSuffix"
	val thursdayTooltip get() = "$saturdayPercent $tooltipSuffix"
	val fridayTooltip get() = "$saturdayPercent $tooltipSuffix"

	fun performShowTooltip(day: DayOfWeek): ChartData = copy(
		showSaturdayTooltip = day == DayOfWeek.SATURDAY,
		showSundayTooltip = day == DayOfWeek.SUNDAY,
		showMondayTooltip = day == DayOfWeek.MONDAY,
		showTuesdayTooltip = day == DayOfWeek.TUESDAY,
		showWednesdayTooltip = day == DayOfWeek.WEDNESDAY,
		showThursdayTooltip = day == DayOfWeek.THURSDAY,
		showFridayTooltip = day == DayOfWeek.FRIDAY,
	)

	companion object {
		@JvmStatic
		fun getValueAsString(originalValue: Int?): String {
			val value = originalValue.orZero()

			return when {
				value >= 1_000_000 -> "${value / 1_000_000} M"
				value >= 1_000 -> "${value / 1_000} K"
				else -> "$value"
			}
		}
	}

}
