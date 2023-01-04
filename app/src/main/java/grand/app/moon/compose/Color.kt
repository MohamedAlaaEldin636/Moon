package grand.app.moon.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

@Immutable
data class ExtendedColors(
	val borderTextField: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
	ExtendedColors(
		borderTextField = Color.Red//Color.Unspecified
	)
}

