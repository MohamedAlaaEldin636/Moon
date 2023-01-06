package grand.app.moon.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

/**
 * - Use your composable in [mainContent] as if there were no popup
 *
 * - Use [fillMaxWidth] for [DropdownMenuItem] as max width is the [mainContent] width isa.
 */
@Composable
fun UIPopupMenuContainer(
	mainContent: @Composable () -> Unit,
	expanded: Boolean,
	onDismissRequest: () -> Unit,
	offset: DpOffset = DpOffset(0.dp, 1.dp),
	properties: PopupProperties = PopupProperties(focusable = true),
	content: @Composable ColumnScope.() -> Unit
) {
	Box {
		var size by remember {
			mutableStateOf(Size(0f, 0f))
		}

		DimensionSubcomposeLayout(
			mainContent = mainContent,
			dependentContent = {
				size = it
			}
		)

		val configuration = LocalConfiguration.current
		val screenHeight = configuration.screenHeightDp.dp
		val height = screenHeight.div(2)

		DropdownMenu(
			modifier = Modifier
				//.fillMaxWidth()
				.background(MaterialTheme.colorScheme.background)
				.width(LocalDensity.current.run { size.toDpSize() }.width)
				.heightIn(max = height),
			offset = offset,
			expanded = expanded,
			onDismissRequest = onDismissRequest,
			properties = properties,
			content = content
		)
	}
}
