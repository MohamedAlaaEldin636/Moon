package grand.app.moon.presentation.myAds.addAdvFinalPage

import androidx.compose.foundation.Image
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import grand.app.moon.R
import grand.app.moon.compose.ExtendedTheme
import grand.app.moon.compose.ui.UIEditText
import grand.app.moon.compose.ui.UIPopupMenuContainer
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.extensions.forEachWithDivider

object AddAdvFinalPageScreenUtils {

	@Composable
	fun DropDown(
		boxModifier: Modifier = Modifier,
		hint: String,
		currentSelection: String,
		options: List<ItemProperty>,
		onSelection: (ItemProperty) -> Unit
	) {
		var expanded by remember {
			mutableStateOf(false)
		}

		val dismissMenu = {
			expanded = false
		}

		UIPopupMenuContainer(
			mainContent = {
				UIEditText.WithBorder.TajawalRegularForm(
					additionalBoxModifier = boxModifier,
					hint = hint,
					isRequired = true,
					text = currentSelection,
					onTextChange = {},
					onClick = { expanded = true },
					suffixComposable = {
						Icon(
							painter = painterResource(id = R.drawable.ic_keyboard_arrow_down_red_24dp),
							contentDescription = "",
							tint = ExtendedTheme.colors.iconTextField
						)
					}
				)
			},
			expanded = expanded,
			onDismissRequest = dismissMenu
		) {
			options.forEachWithDivider(
				dividerAction = {
					Divider()
				}
			) {
				DropdownMenuItem(
					text = {
						Text(text = it.name.orEmpty())
					},
					onClick = {
						onSelection(it)

						dismissMenu()
					}
				)
			}
		}
	}

	@Composable
	fun Switch(
		boxModifier: Modifier = Modifier,
		hint: String,
		currentSelection: Boolean,
		onSelectionAfterToggle: (Boolean) -> Unit
	) {
		UIEditText.WithBorder.TajawalRegularForm(
			additionalBoxModifier = boxModifier,
			hint = hint,
			isRequired = true,
			text = hint,
			onTextChange = {},
			onClick = {
				onSelectionAfterToggle(currentSelection.not())
			},
			suffixComposable = {
				Image(
					painter = painterResource(
						id = if (currentSelection) R.drawable.ic_switch_on_1 else R.drawable.ic_switch_off_1
					),
					contentDescription = null,
				)
			}
		)
	}

}
