package grand.app.moon.presentation.myAds

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import grand.app.moon.R
import grand.app.moon.compose.ExtendedTheme
import grand.app.moon.compose.ui.UIEditText
import grand.app.moon.compose.ui.UIPopupMenuContainer
import grand.app.moon.compose.ui.UIText
import grand.app.moon.extensions.forEachWithDivider
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.base.utils.showMessage
import grand.app.moon.presentation.myAds.addAdvFinalPage.AddAdvFinalPageScreenUtils
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddAdvFinalPageScreen(
	actOnAllPermissionsAcceptedOrRequestPermissions: () -> Unit,
	goGetAddress: () -> Unit,
	showOrGetCities: () -> Unit,
	addAdvertisement: () -> Unit,
	onCameraClick: () -> Unit,
	onGalleryClick: () -> Unit,
) {
	Surface(
		Modifier
			.background(MaterialTheme.colorScheme.background)
			.fillMaxSize()
			.imePadding()
			.padding(16.dp)
			.verticalScroll(rememberScrollState()),
		color = MaterialTheme.colorScheme.background
	) {
		Surface(
			Modifier
				.background(Color.White, RoundedCornerShape(16.dp))
				.clip(RoundedCornerShape(16.dp))
				.padding(16.dp),
			color = Color.White
		) {
			Column(Modifier.fillMaxSize()) {
				val bringIntoViewRequester = remember { BringIntoViewRequester() }

				val focusRequester = remember { FocusRequester() }

				val context = LocalContext.current

				val viewModel = viewModel<AddAdvFinalPageViewModel>()

				AddAdvFinalPageScreenUtils.Images(
					actOnAllPermissionsAcceptedOrRequestPermissions,
					onCameraClick,
					onGalleryClick
				)

				Spacer(modifier = Modifier.height(16.dp))

				val title = viewModel.title.observeAsState()
				UIEditText.WithBorder.TajawalRegularForm(
					hint = context.getString(R.string.address_advertisement),
					isRequired = true,
					text = title.value.orEmpty(),
					onTextChange = { viewModel.title.value = it },
					additionalBoxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)
				)

				Spacer(modifier = Modifier.height(16.dp))

				TextFieldCity(
					showOrGetCities = showOrGetCities,
					boxModifier = Modifier
						.bringIntoViewRequester(bringIntoViewRequester)
						.focusRequester(focusRequester)
				)

				Spacer(modifier = Modifier.height(16.dp))

				val price = viewModel.price.observeAsState()
				UIEditText.WithBorder.TajawalRegularForm(
					hint = context.getString(R.string.price),
					isRequired = true,
					text = price.value.orEmpty(),
					onTextChange = { viewModel.price.value = it },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					additionalBoxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)
				)

				Spacer(modifier = Modifier.height(16.dp))

				val negotiable = viewModel.negotiable.observeAsState()
				UIEditText.WithBorder.TajawalRegularForm(
					additionalBoxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
					hint = context.getString(R.string.price_is_negotiable),
					isRequired = true,
					text = context.getString(R.string.price_is_negotiable),
					onTextChange = {},
					onClick = {
						viewModel.negotiable.value = viewModel.negotiable.value.orFalse().not()
					},
					suffixComposable = {
						Image(
							painter = painterResource(
								id = if (negotiable.value == true) R.drawable.ic_switch_on_1 else R.drawable.ic_switch_off_1
							),
							contentDescription = null,
						)
					}
				)

				if (viewModel.user.isStore == true) {
					Spacer(modifier = Modifier.height(16.dp))

					// todo ... add el se3r abl el khasm isa.
				}

				if (viewModel.user.isStore == true) {
					Spacer(modifier = Modifier.height(16.dp))

					TextFieldStoreCategory()

					Spacer(modifier = Modifier.height(16.dp))

					// todo sub category as well
				}

				if (viewModel.brands.isNotEmpty()) {
					Spacer(modifier = Modifier.height(16.dp))

					TextFieldBrand(boxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester))
				}

				val properties = viewModel.properties.observeAsState()
				val map = viewModel.mapOfProperties.observeAsState()
				for (property in properties.value.orEmpty()) {
					Spacer(modifier = Modifier.height(16.dp))

					when (property.type) {
						1 -> {
							// Multi-Selection
							AddAdvFinalPageScreenUtils.DropDown(
								hint = property.name.orEmpty(),
								currentSelection = map.value?.get(property.id.orZero())?.let { item ->
									item.children?.firstOrNull { it.id == item.valueId }?.name
								}.orEmpty(),
								options = map.value?.get(property.id.orZero())?.children.orEmpty(),
								onSelection = {
									viewModel.mapOfProperties.value = viewModel.mapOfProperties.value.orEmpty().toMutableMap().also { mutableMap ->
										mutableMap[property.id.orZero()] = property.copy(valueId = it.id)
									}.toMap()
								}
							)
						}
						3 -> {
							// Text
							UIEditText.WithBorder.TajawalRegularForm(
								hint = property.name.orEmpty(),
								isRequired = true,
								text = map.value?.get(property.id.orZero())?.valueString.orEmpty(),
								onTextChange = {
									viewModel.mapOfProperties.value = viewModel.mapOfProperties.value.orEmpty().toMutableMap().also { mutableMap ->
										mutableMap[property.id.orZero()] = property.copy(valueString = it)
									}.toMap()
								},
								additionalBoxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)
							)
						}
						else /* null */ -> {
							// Boolean
							AddAdvFinalPageScreenUtils.Switch(
								hint = property.name.orEmpty(),
								currentSelection = map.value?.get(property.id.orZero())?.valueBoolean.orFalse(),
								onSelectionAfterToggle = {
									viewModel.mapOfProperties.value = viewModel.mapOfProperties.value.orEmpty().toMutableMap().also { mutableMap ->
										mutableMap[property.id.orZero()] = property.copy(valueBoolean = it)
									}.toMap()
								}
							)
						}
					}
				}

				Spacer(modifier = Modifier.height(16.dp))

				val locationData = viewModel.locationData.observeAsState()
				UIEditText.WithBorder.TajawalRegularForm(
					hint = context.getString(R.string.location_99),
					isRequired = true,
					text = locationData.value?.address.orEmpty(),
					onTextChange = {},
					onClick = goGetAddress,
					additionalBoxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)
				)

				Spacer(modifier = Modifier.height(16.dp))

				val description = viewModel.description.observeAsState()
				UIEditText.WithBorder.TajawalRegularForm(
					hint = context.getString(R.string.description),
					isRequired = false,
					text = description.value.orEmpty(),
					onTextChange = { viewModel.description.value = it },
					boxModifier = Modifier
						.fillMaxWidth()
						.heightIn(107.dp),
					contentAlignment = Alignment.Top,
					additionalBoxModifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)
				)

				Spacer(modifier = Modifier.height(16.dp))

				Button(
					modifier = Modifier.fillMaxWidth(),
					onClick = {
						addAdvertisement()
					},
					shape = RoundedCornerShape(5.dp),
					contentPadding = PaddingValues(16.dp)
				) {
					UIText.TajawalRegularForm(
						modifier = Modifier
							.fillMaxWidth()
							.align(Alignment.CenterVertically),
						text = stringResource(R.string.addition_12),
						textAlign = TextAlign.Center
					)
				}
			}
		}
	}
}

@Composable
private fun TextFieldCity(
	viewModel: AddAdvFinalPageViewModel = viewModel(),
	context: Context = LocalContext.current,
	showOrGetCities: () -> Unit,
	boxModifier: Modifier = Modifier,
) {
	val expanded = viewModel.showCitiesPopupMenu.observeAsState()

	val dismissMenu = {
		viewModel.showCitiesPopupMenu.value = false
	}

	UIPopupMenuContainer(
		mainContent = {
			val city = viewModel.selectedCity.observeAsState()
			UIEditText.WithBorder.TajawalRegularForm(
				additionalBoxModifier = boxModifier,
				hint = context.getString(R.string.city),
				isRequired = true,
				text = city.value?.name.orEmpty(),
				onTextChange = {},
				onClick = showOrGetCities,
				suffixComposable = {
					Icon(
						painter = painterResource(id = R.drawable.ic_keyboard_arrow_down_red_24dp),
						contentDescription = "",
						tint = ExtendedTheme.colors.iconTextField
					)
				}
			)
		},
		expanded = expanded.value.orFalse(),
		onDismissRequest = dismissMenu
	) {
		viewModel.cities.forEachWithDivider(
			dividerAction = {
				Divider()
			}
		) {
			DropdownMenuItem(
				text = {
					Text(text = it.name)
				},
				onClick = {
					viewModel.selectedCity.value = it

					dismissMenu()
				}
			)
		}
	}
}

@Composable
private fun TextFieldBrand(
	viewModel: AddAdvFinalPageViewModel = viewModel(),
	context: Context = LocalContext.current,
	boxModifier: Modifier = Modifier
) {
	var expanded by remember {
		mutableStateOf(false)
	}

	val dismissMenu = {
		expanded = false
	}

	UIPopupMenuContainer(
		mainContent = {
			val brand = viewModel.selectedBrand.observeAsState()
			UIEditText.WithBorder.TajawalRegularForm(
				hint = context.getString(R.string.brand),
				isRequired = true,
				text = brand.value?.name.orEmpty(),
				onTextChange = {},
				onClick = {
					expanded = true
				},
				suffixComposable = {
					Icon(
						painter = painterResource(id = R.drawable.ic_keyboard_arrow_down_red_24dp),
						contentDescription = "",
						tint = ExtendedTheme.colors.iconTextField
					)
				},
				additionalBoxModifier = boxModifier
			)
		},
		expanded = expanded,
		onDismissRequest = dismissMenu
	) {
		viewModel.brands.forEachWithDivider(
			dividerAction = {
				Divider()
			}
		) {
			DropdownMenuItem(
				text = {
					Text(text = it.name.orEmpty())
				},
				onClick = {
					viewModel.selectedBrand.value = it

					dismissMenu()
				}
			)
		}
	}
}

@Composable
private fun TextFieldStoreCategory(
	viewModel: AddAdvFinalPageViewModel = viewModel(),
	context: Context = LocalContext.current,
) {
	val storeCategory = viewModel.storeCategory.observeAsState()

	var expanded by remember {
		mutableStateOf(false)
	}

	val dismissMenu = {
		expanded = false
	}

	UIPopupMenuContainer(
		mainContent = {
			UIEditText.WithBorder.TajawalRegularForm(
				hint = context.getString(R.string.your_store_category),
				isRequired = true,
				text = storeCategory.value.orEmpty(),
				onTextChange = { viewModel.storeCategory.value = it },
				onClick = {
					// todo get data from api el awal w kda isa.
					expanded = true
				},
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
		DropdownMenuItem(
			text = {
				Text(text = "ewiufi")
			},
			onClick = {

				dismissMenu()
			}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddAdvFinalPageScreen2() {

	Column(
		modifier = Modifier
			.fillMaxSize()
			.imePadding()
			.verticalScroll(rememberScrollState())
	) {

		val bringIntoViewRequester = remember { BringIntoViewRequester() }

		val focusRequester = remember { FocusRequester() }

		List(18) { index ->
			val item = index.toString()

			var text by remember { mutableStateOf(item) }

			TextField(
				value = text,
				onValueChange = { text = it },
				modifier = Modifier
					.bringIntoViewRequester(bringIntoViewRequester).let {
						if (index != 0) it else it.focusRequester(focusRequester)
					},
			)
		}

		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}
	}

}

/*
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddAdvFinalPageScreen() {

	val bringIntoViewRequester = remember { BringIntoViewRequester() }

	val view = LocalView.current

	// Workaround: Call it first to overwrite the setWindowInsetsAnimationCallback set by Compose.
	WindowInsets.ime

	LaunchedEffect(Unit) {
		ViewCompat.setWindowInsetsAnimationCallback(view, null)
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.imePadding()
			.verticalScroll(rememberScrollState())
	) {

		List(18) { index ->
			val item = index.toString()

			var text by remember { mutableStateOf(item) }

			TextField(
				value = text,
				onValueChange = { text = it },
				modifier = Modifier
					.bringIntoViewRequester(bringIntoViewRequester)
			)
		}
	}

}
 */