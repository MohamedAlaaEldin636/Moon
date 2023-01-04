package grand.app.moon.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BaseTheme(
	modifier: Modifier = Modifier,
	background: @Composable () -> Color = { MaterialTheme.colorScheme.background },
	content: @Composable () -> Unit
) = MyBaseTheme(modifier.fillMaxSize(), background, content)

@Composable
fun PreviewBaseTheme(
	modifier: Modifier = Modifier,
	background: @Composable () -> Color = { MaterialTheme.colorScheme.background },
	content: @Composable () -> Unit
) = MyBaseTheme(modifier, background, content)

@Composable
private fun MyBaseTheme(
	modifier: Modifier,
	background: @Composable () -> Color,
	content: @Composable () -> Unit
) {
	val darkTheme = isSystemInDarkTheme()

	ComposeMoonTheme(darkTheme) {
		val systemUiController = rememberSystemUiController()

		val statusBarColor = if (darkTheme) {
			MaterialTheme.colorScheme.primary // surfaceVariant
		}else {
			MaterialTheme.colorScheme.primary
		}

		LaunchedEffect(systemUiController) {
			systemUiController.setStatusBarColor(statusBarColor)
		}

		Surface(
			modifier = modifier,
			color = background()
		) {
			content()
		}
	}
}
