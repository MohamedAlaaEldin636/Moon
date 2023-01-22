package grand.app.moon.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
	primary = Color(13, 6, 51),
	onPrimary = Color.White,
	primaryContainer = Color(229, 222, 255),
	onPrimaryContainer = Color(24, 3, 98),

	secondary = Teal200,

	tertiary = Color(238, 153, 54),
	onTertiary = Color.White,
	tertiaryContainer = Color(225, 220, 189),
	onTertiaryContainer = Color(44, 22, 0),
)

private val LightColorPalette = lightColorScheme(
	primary = Color(13, 6, 51),
	onPrimary = Color.White,
	primaryContainer = Color(229, 222, 255),
	onPrimaryContainer = Color(24, 3, 98),

	secondary = Teal200,

	tertiary = Color(238, 153, 54),
	onTertiary = Color.White,
	tertiaryContainer = Color(225, 220, 189),
	onTertiaryContainer = Color(44, 22, 0),

	background = Color(251, 251, 251)

	/* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeMoonTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val colors = if (darkTheme) {
		DarkColorPalette
	} else {
		LightColorPalette
	}

	val extendedColors = ExtendedColors(
		borderTextField = Color(175, 175, 175),
		dialogScrim = Color.Black.copy(alpha = 0.25f),
		hintColor = Color(158, 158, 158),
		requiredColor = Color(255, 0, 0),
		iconTextField = Color(114, 120, 137),
		imagesBorder = Color(206, 206, 206),
	)

	val extendedTypography = defaultExtendedTypography.copy()

	CompositionLocalProvider(
		LocalExtendedColors provides extendedColors,
		LocalExtendedTypography provides extendedTypography
	) {
		MaterialTheme(
			colorScheme = colors,
			typography = Typography,
			shapes = Shapes,
			content = content
		)
	}
}

// Use with eg. ExtendedTheme.colors.tertiary

object ExtendedTheme {
	val colors: ExtendedColors
		@Composable
		get() = LocalExtendedColors.current

	val typography: ExtendedTypography
		@Composable
		get() = LocalExtendedTypography.current
}

/*


@Composable
fun ExtendedTheme(
	/* ... */
	content: @Composable () -> Unit
) {
	val extendedColors = ExtendedColors(
		borderTextField = Color(175, 175, 175)
	)
	CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
		MaterialTheme(
			/* colors = ..., typography = ..., shapes = ... */
			content = content
		)
	}
}
 */
