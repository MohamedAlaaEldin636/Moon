package grand.app.moon.presentation.myAds.addAdvFinalPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import grand.app.moon.core.extenstions.showError
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.compose.GlideImageViaXml
import grand.app.moon.extensions.compose.GlideImageViaXmlModel
import grand.app.moon.extensions.forEachWithDivider
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragment
import grand.app.moon.presentation.myAds.model.LocalOrApiItemImage
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
						Image(
							painter = painterResource(id = R.drawable.ic_arrow_down_d999),
							contentDescription = "",
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
		viewModel: AddAdvFinalPageViewModel = viewModel(),
		deleteImageFromApi: (id: Int, onSuccess: () -> Unit) -> Unit,
	) {
		val expanded = false//viewModel.showImagesPopupMenu.observeAsState()

		val dismissMenu = {
			//viewModel.showImagesPopupMenu.value = false
		}

		val context = LocalContext.current

		UIPopupMenuContainer(
			mainContent = {
				ImagesInsideContainer(
					actOnAllPermissionsAcceptedOrRequestPermissions, 
					onCameraClick = onCameraClick, 
					onGalleryClick = onGalleryClick,
					deleteImageFromApi = deleteImageFromApi
				)
			},
			expanded = expanded/*.value.orFalse()*/,
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
		onCameraClick: () -> Unit,
		onGalleryClick: () -> Unit,
		deleteImageFromApi: (id: Int, onSuccess: () -> Unit) -> Unit,
	) {
		Box {
			var size by remember {
				mutableStateOf(Size(0f, 0f))
			}

			DimensionSubcomposeLayout(
				mainContent = {
					if (true) {
						ImagesInsideContainerMainContent(
							actOnAllPermissionsAcceptedOrRequestPermissions,
							viewModel,
							onCameraClick,
							onGalleryClick,
							deleteImageFromApi,
						)

						return@DimensionSubcomposeLayout
					}

					/*val mapOfImages = viewModel.mapOfImages.observeAsState()

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

								*//*LazyVerticalGrid(
									columns = GridCells.Fixed(3),
								) {
									items(photos) { photo ->
										PhotoItem(photo)
									}
								}*//*
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
					}*/
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

	@OptIn(ExperimentalGlideComposeApi::class)
	@Composable
	private fun ImagesInsideContainerMainContent(
		actOnAllPermissionsAcceptedOrRequestPermissions: () -> Unit,
		viewModel: AddAdvFinalPageViewModel = viewModel(),
		onCameraClick: () -> Unit,
		onGalleryClick: () -> Unit,
		deleteImageFromApi: (id: Int, onSuccess: () -> Unit) -> Unit,
	) {
		//val mapOfImages = viewModel.mapOfImages.observeAsState()
		val listOfImages = viewModel.listOfImages.observeAsState()

		val minimumValue = if (listOfImages.value?.size.orZero() < 6) 6 else {
			listOfImages.value?.size.orZero().inc().coerceAtMost(22)
		}

		val numberOfRows = (listOfImages.value?.size.orZero().coerceAtLeast(minimumValue).dec() / 3).inc()

		/*96.dp fixed of cell and all paddings are 8.dp*/
		val height = (96.dp.times(numberOfRows)) + (8.dp.times(numberOfRows/*.inc()*/))
		LazyVerticalGrid(
			modifier = Modifier
				.fillMaxWidth()
				.padding(4.dp)
				.height(height),
			columns = GridCells.Fixed(3),
		) {
			items(listOfImages.value.orEmpty().size.coerceAtLeast(minimumValue)) { index ->
				Box(
					Modifier
						.fillMaxWidth()
						.padding(4.dp)
				) {
					val expanded = viewModel.indexToShowImagesPopupMenu.observeAsState()

					val dismissMenu = {
						viewModel.indexToShowImagesPopupMenu.value = null
					}

					val context = LocalContext.current

					UIPopupMenuContainer(
						mainContent = {
							val modifierInnerBox = Modifier
								.fillMaxWidth()
								.height(96.dp)
								.clickable {
									viewModel.beforeCheckPermissionsIndexToShowImagesPopupMenu.value = index

									actOnAllPermissionsAcceptedOrRequestPermissions()
								}
							if (listOfImages.value.orEmpty().getOrNull(index) == null) {
								Box(modifierInnerBox) {
									if (index == 0) {
										MainImagePlaceholder()
									}else {
										SubImagePlaceholder()
									}
								}
							}else {
								key(listOfImages.value) {
									Box(
										modifierInnerBox
											.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
											//.border(1.dp, ExtendedTheme.colors.imagesBorder, RoundedCornerShape(5.dp))
											.clip(RoundedCornerShape(5.dp))
									) {
										GlideImageViaXml(
											modifier = Modifier.fillMaxSize(),
											model = listOfImages.value.orEmpty().getOrNull(index)?.let { item ->
												when (item) {
													is LocalOrApiItemImage.Api -> GlideImageViaXmlModel.IString(item.itemImage.image)
													is LocalOrApiItemImage.Local -> GlideImageViaXmlModel.IUri(item.uri)
												}
											},
											//contentDescription = null
										)

										val listOfImages2 = viewModel.listOfImages.observeAsState()

										Box(Modifier.padding(4.dp)) {
											Image(
												modifier = Modifier.clickable {
													val item = viewModel.listOfImages.value?.getOrNull(index) ?: return@clickable
													
													if (item is LocalOrApiItemImage.Api) {
														deleteImageFromApi(item.itemImage.id.orZero()) {
															viewModel.listOfImages.value = viewModel.listOfImages.value.orEmpty().toMutableList().also {
																MyLogger.e("index $index, item ${it.getOrNull(index)}")
																
																it.removeAt(index)
															}.toList()
														}
													}else if (viewModel.response != null && viewModel.listOfImages.value.orEmpty().size == 1) {
														context.showError(context.getString(R.string.at_least_1_image_898))
													}else {
														viewModel.listOfImages.value = viewModel.listOfImages.value.orEmpty().toMutableList().also {
															MyLogger.e("index $index, item ${it.getOrNull(index)}")

															it.removeAt(index)
														}.toList()
													}
												},
												painter = painterResource(id = R.drawable.ic_close_876),
												contentDescription = System.currentTimeMillis().plus(
													listOfImages2.value.orEmpty().size.toLong()
												).toString()
											)
										}
									}
								}
							}
						},
						expanded = expanded.value == index,
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
			}
		}
	}

	@Composable
	fun BoxScope.MainImagePlaceholder(
		viewModel: AddAdvFinalPageViewModel = viewModel()
	) {
		val context = LocalContext.current

		Surface(
			shadowElevation = 0.dp
		) {
			Box(
				Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
					.border(0.2.dp, ExtendedTheme.colors.imagesBorder, RoundedCornerShape(5.dp))
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

					UIText.TajawalRegularForm(
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
			shadowElevation = 0.dp
		) {
			Column(
				Modifier
					.fillMaxSize()
					.background(Color.White, RoundedCornerShape(5.dp))
					.border(0.2.dp, ExtendedTheme.colors.imagesBorder, RoundedCornerShape(5.dp)),
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
