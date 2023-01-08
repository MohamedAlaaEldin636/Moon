package grand.app.moon.presentation.myAds.addAdvFinalPage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import grand.app.moon.R
import grand.app.moon.compose.ExtendedTheme
import grand.app.moon.compose.ui.DimensionSubcomposeLayout
import grand.app.moon.compose.ui.UIEditText
import grand.app.moon.compose.ui.UIPopupMenuContainer
import grand.app.moon.compose.ui.UIText
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.extensions.forEachWithDivider
import grand.app.moon.extensions.orFalse
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragment
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel

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

	@Composable
	fun Images(
		actOnAllPermissionsAcceptedOrRequestPermissions: () -> Unit,
		onCameraClick: () -> Unit,
		onGalleryClick: () -> Unit,
		viewModel: AddAdvFinalPageViewModel = viewModel()
	) {
		val expanded = viewModel.showImagesPopupMenu.observeAsState()

		val dismissMenu = {
			viewModel.showImagesPopupMenu.value = false
		}

		val context = LocalContext.current

		UIPopupMenuContainer(
			mainContent = {
				ImagesInsideContainer(actOnAllPermissionsAcceptedOrRequestPermissions)
			},
			expanded = expanded.value.orFalse(),
			onDismissRequest = dismissMenu
		) {
			DropdownMenuItem(
				text = {
					Text(text = context.getString(R.string.camera))
				},
				onClick = {
					onCameraClick()

					dismissMenu()
				}
			)

			Divider()

			DropdownMenuItem(
				text = {
					Text(text = context.getString(R.string.gallery))
				},
				onClick = {
					onGalleryClick()

					dismissMenu()
				}
			)
		}
	}

	@OptIn(ExperimentalGlideComposeApi::class)
	@Composable
	private fun ImagesInsideContainer(
		actOnAllPermissionsAcceptedOrRequestPermissions: () -> Unit,
		viewModel: AddAdvFinalPageViewModel = viewModel(),
	) {
		Box {
			var size by remember {
				mutableStateOf(Size(0f, 0f))
			}

			DimensionSubcomposeLayout(
				mainContent = {
					val mapOfImages = viewModel.mapOfImages.observeAsState()

					Column(
						Modifier
							.fillMaxWidth()
							.padding(8.dp)) {
						Row(
							Modifier
								.fillMaxWidth()
								.height(96.dp)
						) {
							Box(
								Modifier
									.weight(1f)
									.fillMaxHeight()
									.clickable {
										CameraUtils.tag = 1

										actOnAllPermissionsAcceptedOrRequestPermissions()
									}
							) {
								val uri = mapOfImages.value?.get(1)?.firstOrNull()

								if (uri == null) {
									MainImagePlaceholder()
								}else {
									Box(
										Modifier
											.fillMaxSize()
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
									) {
										GlideImage(
											modifier = Modifier
												.fillMaxSize()
												.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
											model = uri,
											contentDescription = null
										)

										val count = viewModel.mapOfImages.observeAsState()

										if (count.value?.get(1).orEmpty().size > 1) {
											Column(
												Modifier
													.fillMaxSize()
													.background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(5.dp)),
												verticalArrangement = Arrangement.Center,
												horizontalAlignment = Alignment.CenterHorizontally,
											) {
												UIText.TajawalMediumForm(
													text = count.value?.get(1).orEmpty().size.toString(),
													color = Color.White,
													textAlign = TextAlign.Center,
													textSize = 20.sp
												)
											}
										}
									}
								}
							}

							Spacer(Modifier.width(8.dp))

							Box(
								Modifier
									.weight(1f)
									.fillMaxHeight()
									.clickable {
										CameraUtils.tag = 2

										actOnAllPermissionsAcceptedOrRequestPermissions()
									}
							) {
								val uri = mapOfImages.value?.get(2)?.firstOrNull()

								if (uri == null) {
									SubImagePlaceholder()
								}else {
									GlideImage(
										modifier = Modifier
											.fillMaxSize()
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
										model = uri,
										contentDescription = null
									)
								}
							}

							Spacer(Modifier.width(8.dp))

							Box(
								Modifier
									.weight(1f)
									.fillMaxHeight()
									.clickable {
										CameraUtils.tag = 3

										actOnAllPermissionsAcceptedOrRequestPermissions()
									}
							) {
								val uri = mapOfImages.value?.get(3)?.firstOrNull()

								if (uri == null) {
									SubImagePlaceholder()
								}else {
									GlideImage(
										modifier = Modifier
											.fillMaxSize()
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
										model = uri,
										contentDescription = null
									)
								}
							}
						}
						
						Spacer(modifier = Modifier.height(8.dp))

						Row(
							Modifier
								.fillMaxWidth()
								.height(96.dp)
						) {
							Box(
								Modifier
									.weight(1f)
									.fillMaxHeight()
									.clickable {
										CameraUtils.tag = 4

										actOnAllPermissionsAcceptedOrRequestPermissions()
									}
							) {
								val uri = mapOfImages.value?.get(4)?.firstOrNull()

								if (uri == null) {
									SubImagePlaceholder()
								}else {
									GlideImage(
										modifier = Modifier
											.fillMaxSize()
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
										model = uri,
										contentDescription = null
									)
								}
							}

							Spacer(Modifier.width(8.dp))

							Box(
								Modifier
									.weight(1f)
									.fillMaxHeight()
									.clickable {
										CameraUtils.tag = 5

										actOnAllPermissionsAcceptedOrRequestPermissions()
									}
							) {
								val uri = mapOfImages.value?.get(5)?.firstOrNull()

								if (uri == null) {
									SubImagePlaceholder()
								}else {
									GlideImage(
										modifier = Modifier
											.fillMaxSize()
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
										model = uri,
										contentDescription = null
									)
								}
							}

							Spacer(Modifier.width(8.dp))

							Box(
								Modifier
									.weight(1f)
									.fillMaxHeight()
									.clickable {
										CameraUtils.tag = 6

										actOnAllPermissionsAcceptedOrRequestPermissions()
									}
							) {
								val uri = mapOfImages.value?.get(6)?.firstOrNull()

								if (uri == null) {
									SubImagePlaceholder()
								}else {
									GlideImage(
										modifier = Modifier
											.fillMaxSize()
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
										model = uri,
										contentDescription = null
									)
								}
							}
						}
					}
				},
				dependentContent = {
					size = it
				}
			)

			val stroke = Stroke(
				width = 2f,
				pathEffect = PathEffect.dashPathEffect(floatArrayOf(13f, 13f), 0f)
			)
			Canvas(
				Modifier
					.fillMaxWidth()
					.height(LocalDensity.current.run { size.toDpSize() }.height)
			){
				drawRoundRect(color = Color(181, 181, 181), style = stroke)
			}
		}
	}

	@Composable
	fun BoxScope.MainImagePlaceholder(
		viewModel: AddAdvFinalPageViewModel = viewModel()
	) {
		val context = LocalContext.current

		Surface(
			shadowElevation = 4.dp
		) {
			Box(
				Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
			) {
				Column(
					Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Image(
						painter = painterResource(
							id = R.drawable.ic_photo_main_placeholder
						),
						contentDescription = null,
					)

					Spacer(modifier = Modifier.height(6.dp))

					UIText.TajawalMediumForm(
						text = context.getString(R.string.home),
						color = Color.White,
						textAlign = TextAlign.Center
					)
				}
			}
		}
	}

	@Composable
	fun BoxScope.SubImagePlaceholder() {
		Surface(
			shadowElevation = 4.dp
		) {
			Column(
				Modifier
					.fillMaxSize()
					.background(Color.White, RoundedCornerShape(5.dp)),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Image(
					painter = painterResource(
						id = R.drawable.ic_photo_sub_placeholder
					),
					contentDescription = null,
				)
			}
		}
	}

}
