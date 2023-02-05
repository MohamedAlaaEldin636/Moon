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

	val week = (if (isCurrentWeek) currentWeek else previousWeek).orEmpty()/*.mapIndexed { index, int ->
		if (isCurrentWeek) index else 6 - index
	}*/
	val maxWeekLineValue = week.maxOf { it }.coerceAtLeast(6) // 7 lines where line 1 is always 0 isa.
	MyLogger.e("toChartData -> maxWeekLineValue -> $maxWeekLineValue, week $week")

	val line1 = 0
	val step = ((maxWeekLineValue.toFloat() - line1.toFloat()) / 6f).roundToInt()
	val line2 = line1 + step
	val line3 = line2 + step
	val line4 = line3 + step
	val line5 = line4 + step
	val line6 = line5 + step
	val line7 = line6 + step
	MyLogger.e("toChartData -> lines -> $line7 $line6 $line5 $line4 $line3 $line2 $line1 ==== step -> $step")

	val saturdayValue = week.getOrNull(0).orZero()
	val sundayValue = week.getOrNull(1).orZero()
	val mondayValue = week.getOrNull(2).orZero()
	val tuesdayValue = week.getOrNull(3).orZero()
	val wednesdayValue = week.getOrNull(4).orZero()
	val thursdayValue = week.getOrNull(5).orZero()
	val fridayValue = week.getOrNull(6).orZero()

	MyLogger.e("toChartData -> saturdayValue -> $saturdayValue $sundayValue $mondayValue $tuesdayValue ...")

	val saturdayPercent = saturdayValue.toFloat() / line7.toFloat() * 100f
	MyLogger.e("toChartData -> saturdayPercent -> ${week.getOrNull(0)} ${line7.toFloat()} $saturdayPercent")
	val sundayPercent = sundayValue.toFloat() / line7.toFloat() * 100f
	MyLogger.e("toChartData -> sundayPercent -> ${week.getOrNull(1)} ${line7.toFloat()} $sundayPercent")
	val mondayPercent = mondayValue.toFloat() / line7.toFloat() * 100f
	MyLogger.e("toChartData -> mondayPercent -> ${week.getOrNull(2)} ${line7.toFloat()} $mondayPercent")
	val tuesdayPercent = tuesdayValue.toFloat() / line7.toFloat() * 100f
	val wednesdayPercent = wednesdayValue.toFloat() / line7.toFloat() * 100f
	val thursdayPercent = thursdayValue.toFloat() / line7.toFloat() * 100f
	val fridayPercent = fridayValue.toFloat() / line7.toFloat() * 100f
	MyLogger.e("toChartData -> fridayPercent -> ${week.getOrNull(6)} ${line7.toFloat()} $fridayPercent")

	return ChartData(
		args.titlePlural,
		if (isCurrentWeek) context.getString(R.string.current_week) else context.getString(R.string.prev_week),
		percentage.toString(),
		if (percentage >= 0.toBigDecimal()) R.drawable.ic_positive_growth else R.drawable.ic_negative_growth,
		args.titleSingular,
		line7, line6, line5, line4, line3, line2, line1,
		saturdayValue, sundayValue, mondayValue, tuesdayValue, wednesdayValue, thursdayValue, fridayValue,
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

	val saturdayValue: Int = 0,
	val sundayValue: Int = 0,
	val mondayValue: Int = 0,
	val tuesdayValue: Int = 0,
	val wednesdayValue: Int = 0,
	val thursdayValue: Int = 0,
	val fridayValue: Int = 0,

	@FloatRange(from = 0.0, to = 100.0) val saturdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 100.0) val sundayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 100.0) val mondayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 100.0) val tuesdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 100.0) val wednesdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 100.0) val thursdayPercent: Float = 0f,
	@FloatRange(from = 0.0, to = 100.0) val fridayPercent: Float = 0f,

	val showSaturdayTooltip: Boolean = false,
	val showSundayTooltip: Boolean = false,
	val showMondayTooltip: Boolean = false,
	val showTuesdayTooltip: Boolean = false,
	val showWednesdayTooltip: Boolean = false,
	val showThursdayTooltip: Boolean = false,
	val showFridayTooltip: Boolean = false,
) {

	val saturdayTooltip get() = "$saturdayValue $tooltipSuffix"
	val sundayTooltip get() = "$sundayValue $tooltipSuffix"
	val mondayTooltip get() = "$mondayValue $tooltipSuffix"
	val tuesdayTooltip get() = "$tuesdayValue $tooltipSuffix"
	val wednesdayTooltip get() = "$wednesdayValue $tooltipSuffix"
	val thursdayTooltip get() = "$thursdayValue $tooltipSuffix"
	val fridayTooltip get() = "$fridayValue $tooltipSuffix"

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
