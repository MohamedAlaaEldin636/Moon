package grand.app.moon.domain.stats

import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import grand.app.moon.R

/**
 * @param percentage scale is 2 decimal places only.
 * @param saturdayPercent from 0.0 to 1.0 where 0 is zero and 1.0 represents max value [line7] isa.
 * @param percentageIconRes [R.drawable.ic_positive_growth] || [R.drawable.ic_negative_growth]
 */
data class ChartData(
	val dataName: String,
	val weekName: String,
	val percentage: String,
	@DrawableRes val percentageIconRes: Int,

	val tooltipSuffix: String,

	val line7: Int,
	val line6: Int,
	val line5: Int,
	val line4: Int,
	val line3: Int,
	val line2: Int,
	val line1: Int,

	@FloatRange(from = 0.0, to = 1.0) val saturdayPercent: Float,
	@FloatRange(from = 0.0, to = 1.0) val sundayPercent: Float,
	@FloatRange(from = 0.0, to = 1.0) val mondayPercent: Float,
	@FloatRange(from = 0.0, to = 1.0) val tuesdayPercent: Float,
	@FloatRange(from = 0.0, to = 1.0) val wednesdayPercent: Float,
	@FloatRange(from = 0.0, to = 1.0) val thursdayPercent: Float,
	@FloatRange(from = 0.0, to = 1.0) val fridayPercent: Float,
) {

	companion object {
		@JvmStatic
		fun getValueAsString(value: Int): String {
			return when {
				value >= 1_000_000 -> "${value / 1_000_000} M"
				value >= 1_000 -> "${value / 1_000} K"
				else -> "$value"
			}
		}
	}

}
