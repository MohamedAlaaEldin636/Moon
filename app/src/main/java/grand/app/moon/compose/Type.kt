package grand.app.moon.compose

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import grand.app.moon.R

/*
@Immutable
data class ExtendedColors(
	val borderTextField: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
	ExtendedColors(
		borderTextField = Color.Red//Color.Unspecified
	)
}
 */

private val tajawalFontFamily = FontFamily(
	Font(R.font.tajawal_regular, FontWeight.Normal),
	Font(R.font.tajawal_medium, FontWeight.Medium),
	Font(R.font.tajawal_bold, FontWeight.Bold),
)

@Immutable
data class ExtendedTypography(
	val tajawalRegular: TextStyle,
	val tajawalMedium: TextStyle,
	val tajawalBold: TextStyle,
)

val LocalExtendedTypography = staticCompositionLocalOf {
	defaultExtendedTypography
}

val defaultExtendedTypography = ExtendedTypography(
	tajawalRegular = TextStyle(
		fontFamily = tajawalFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp
	),
	tajawalMedium = TextStyle(
		fontFamily = tajawalFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp
	),
	tajawalBold = TextStyle(
		fontFamily = tajawalFontFamily,
		fontWeight = FontWeight.Bold,
		fontSize = 14.sp
	),
)

// Set of Material typography styles to start with
val Typography = Typography(
	bodyLarge = TextStyle(
		fontFamily = FontFamily.Default,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp
	)
	/* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)