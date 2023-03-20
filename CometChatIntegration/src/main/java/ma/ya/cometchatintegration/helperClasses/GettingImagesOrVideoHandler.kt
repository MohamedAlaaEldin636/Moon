package ma.ya.cometchatintegration.helperClasses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ma.ya.cometchatintegration.R
import ma.ya.cometchatintegration.extensions.*

fun Fragment.createPickImagesOrVideoHandlerForSingleImageFromCamera(
	onReceive: (uri: Uri) -> Unit
) = PickImagesOrVideoHandler(
	this,
	PickImagesOrVideoHandler.SupportedMediaType.IMAGE,
	PickImagesOrVideoHandler.SourceOfData.Camera,
	requestMultipleImages = false,
	onReceive = { uris, _, _ ->
		uris.firstOrNull()?.also { onReceive(it) }
	}
)
fun Fragment.createPickImagesOrVideoHandlerForSingleImageFromGallery(
	onReceive: (uri: Uri) -> Unit
) = PickImagesOrVideoHandler(
	this,
	PickImagesOrVideoHandler.SupportedMediaType.IMAGE,
	PickImagesOrVideoHandler.SourceOfData.Gallery,
	requestMultipleImages = false,
	onReceive = { uris, _, _ ->
		uris.firstOrNull()?.also { onReceive(it) }
	}
)
fun Fragment.createPickImagesOrVideoHandlerForVideoFromCamera(
	onReceive: (uri: Uri) -> Unit
) = PickImagesOrVideoHandler(
	this,
	PickImagesOrVideoHandler.SupportedMediaType.VIDEO,
	PickImagesOrVideoHandler.SourceOfData.Camera,
	maxVideoLengthInSeconds = 5 * 60,
	requestMultipleImages = false,
	onReceive = { uris, _, _ ->
		uris.firstOrNull()?.also { onReceive(it) }
	}
)
fun Fragment.createPickImagesOrVideoHandlerForVideoFromGallery(
	onReceive: (uri: Uri) -> Unit
) = PickImagesOrVideoHandler(
	this,
	PickImagesOrVideoHandler.SupportedMediaType.VIDEO,
	PickImagesOrVideoHandler.SourceOfData.Gallery,
	maxVideoLengthInSeconds = 5 * 60,
	requestMultipleImages = false,
	onReceive = { uris, _, _ ->
		uris.firstOrNull()?.also { onReceive(it) }
	}
)

/**
 * - Use [requestImageOrVideo] and permissions will be auto handled then you can use constructor
 * params to do whatever you want.
 */
class PickImagesOrVideoHandler(
	fragment: Fragment,
	private val supportedMediaType: SupportedMediaType,
	private val sourceOfData: SourceOfData = SourceOfData.BOTH,
	private val maxVideoLengthInSeconds: Int = 3 * 60, // Ex. 3 minutes -> 3 * 60
	private val requestMultipleImages: Boolean = false,
	private val getAnchor: (tag: Bundle) -> View? = { null },
	private val onReceive: (uris: List<Uri>, fromCamera: Boolean, isImageNotVideo: Boolean) -> Unit,
) : PermissionsHandler.Listener {

	private val galleryPermissions = buildList {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			when (supportedMediaType) {
				SupportedMediaType.IMAGE -> add(Manifest.permission.READ_MEDIA_IMAGES)
				SupportedMediaType.VIDEO -> add(Manifest.permission.READ_MEDIA_VIDEO)
				SupportedMediaType.BOTH -> {
					add(Manifest.permission.READ_MEDIA_IMAGES)
					add(Manifest.permission.READ_MEDIA_VIDEO)
				}
			}
		}else {
			add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			add(Manifest.permission.READ_EXTERNAL_STORAGE)
		}
	}
	private val cameraPermissions = listOf(Manifest.permission.CAMERA)

	private val handler = PermissionsHandler(
		fragment,
		fragment.lifecycle,
		fragment.requireContext(),
		when (sourceOfData) {
			SourceOfData.Gallery -> galleryPermissions
			SourceOfData.Camera -> cameraPermissions
			SourceOfData.BOTH -> galleryPermissions + cameraPermissions
		},
		this
	)

	private var tag = Bundle.EMPTY

	private val activityResultVideoCamera = if (supportedMediaType == SupportedMediaType.IMAGE) null else fragment.registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		if (it.resultCode == Activity.RESULT_OK) {
			val uri = it.data?.data ?: return@registerForActivityResult

			val context = (handler.weakRefHost.get() as? Fragment)?.context ?: return@registerForActivityResult

			if (!uri.checkSizeAndLengthOfVideo(context, maxVideoLengthInSeconds)) {
				context.showError(context.getString(R.string.max_video_hint_3_min))

				return@registerForActivityResult
			}

			onReceive(listOf(uri), true, false)
		}
	}

	private val activityResultVideoGallery = if (supportedMediaType == SupportedMediaType.IMAGE) null else fragment.registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		if (it.resultCode == Activity.RESULT_OK) {
			val uri = it.data?.data ?: return@registerForActivityResult

			val context = (handler.weakRefHost.get() as? Fragment)?.context ?: return@registerForActivityResult

			if (!uri.checkSizeAndLengthOfVideo(context, maxVideoLengthInSeconds)) {
				context.showError(context.getString(R.string.max_video_hint_3_min))

				return@registerForActivityResult
			}

			onReceive(listOf(uri), false, false)
		}
	}

	private val activityResultImageCameraFile = if (supportedMediaType == SupportedMediaType.VIDEO) null else fragment.registerForActivityResult(
		ActivityResultContracts.TakePicture()
	) { result ->
		if (result != null && result) {
			CameraUtils.imageUri?.also { imageUri ->
				onReceive(listOf(imageUri), true, true)
			}
		}
	}

	private val activityResultImageGallery = if (supportedMediaType == SupportedMediaType.VIDEO) null else fragment.registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		MyLogger.e("on result -> it.resultCode == Activity.RESULT_OK ${it.resultCode} ${Activity.RESULT_OK}")
		if (it.resultCode == Activity.RESULT_OK) {
			val list = if (requestMultipleImages) {
				it.data?.clipData?.let { clipData ->
					List(clipData.itemCount) { index ->
						clipData.getItemAt(index).uri
					}.filterNotNull()
				}.orIfNullOrEmpty {
					listOfNotNull(it.data?.data)
				}
			}else {
				listOfNotNull(it.data?.data)
			}

			onReceive(list, false, true)
		}
	}

	fun requestImageOrVideo(tag: Bundle = Bundle.EMPTY) {
		this.tag = tag

		handler.actOnAllPermissionsAcceptedOrRequestPermissions()
	}

	private fun pickImageFromCamera() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		activityResultImageCameraFile?.launchSafely(
			fragment.context,
			CameraUtils.createImageUri(fragment.activity ?: return)
		)
	}

	private fun pickImageFromGallery() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		if (requestMultipleImages) {
			val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
				it.type = "image/*"
				it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
			}

			activityResultImageGallery?.launchSafely(
				fragment.context,
				intent.createChooserMA(fragment.getString(R.string.pick_image))
			)
		}else {
			val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

			activityResultImageGallery?.launchSafely(
				fragment.context,
				intent.createChooserMA(fragment.getString(R.string.pick_image))
			)
		}
	}

	private fun showPickerForImageOrVideoFromGallery() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		val galleryImage = "${fragment.getString(R.string.gallery)} (${fragment.getString(R.string.image)})"
		val galleryVideo = "${fragment.getString(R.string.gallery)} (${fragment.getString(R.string.video)})"

		getAnchor(tag)?.showPopup(
			listOf(
				galleryImage, galleryVideo
			)
		) {
			when (it.title.toStringOrEmpty()) {
				galleryImage -> pickImageFromGallery()
				galleryVideo -> pickVideoFromGallery()
			}
		}
	}
	private fun showPickerForImageOrVideoFromCamera() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		val cameraImage = "${fragment.getString(R.string.camera)} (${fragment.getString(R.string.image)})"
		val cameraVideo = "${fragment.getString(R.string.camera)} (${fragment.getString(R.string.video)})"

		getAnchor(tag)?.showPopup(
			listOf(
				cameraImage, cameraVideo
			)
		) {
			when (it.title.toStringOrEmpty()) {
				cameraImage -> pickImageFromCamera()
				cameraVideo -> pickVideoFromCamera()
			}
		}
	}
	private fun showPickerForVideoFromCameraOrGallery() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		val videoCamera = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.camera)})"
		val videoGallery = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.gallery)})"

		getAnchor(tag)?.showPopup(
			listOf(
				videoCamera, videoGallery
			)
		) {
			when (it.title.toStringOrEmpty()) {
				videoCamera -> pickVideoFromCamera()
				videoGallery -> pickVideoFromGallery()
			}
		}
	}
	private fun showPickerForImageFromCameraOrGallery() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		val imageCamera = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.camera)})"
		val imageGallery = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.gallery)})"

		getAnchor(tag)?.showPopup(
			listOf(
				imageCamera, imageGallery
			)
		) {
			when (it.title.toStringOrEmpty()) {
				imageCamera -> pickImageFromCamera()
				imageGallery -> pickImageFromGallery()
			}
		}
	}
	private fun showPickerForImageOrVideoFromCameraOrGallery() {
		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		val imageCamera = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.camera)})"
		val imageGallery = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.gallery)})"

		val videoCamera = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.camera)})"
		val videoGallery = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.gallery)})"

		getAnchor(tag)?.showPopup(
			listOf(
				imageCamera, imageGallery, videoCamera, videoGallery
			)
		) {
			when (val title = it.title.toStringOrEmpty()) {
				imageCamera, imageGallery -> pickImage(title == imageCamera)
				videoCamera, videoGallery -> pickVideo(title == videoCamera)
			}
		}
	}

	override fun onAllPermissionsAccepted() {
		when (sourceOfData) {
			SourceOfData.Gallery -> when (supportedMediaType) {
				SupportedMediaType.VIDEO -> pickVideoFromGallery()
				SupportedMediaType.IMAGE -> pickImageFromGallery()
				SupportedMediaType.BOTH -> showPickerForImageOrVideoFromGallery()
			}
			SourceOfData.Camera -> when (supportedMediaType) {
				SupportedMediaType.VIDEO -> pickVideoFromCamera()
				SupportedMediaType.IMAGE -> pickImageFromCamera()
				SupportedMediaType.BOTH -> showPickerForImageOrVideoFromCamera()
			}
			SourceOfData.BOTH -> when (supportedMediaType) {
				SupportedMediaType.VIDEO -> showPickerForVideoFromCameraOrGallery()
				SupportedMediaType.IMAGE -> showPickerForImageFromCameraOrGallery()
				SupportedMediaType.BOTH -> showPickerForImageOrVideoFromCameraOrGallery()
			}
		}
	}

	private fun pickImage(fromCamera: Boolean) {
		if (fromCamera) {
			pickImageFromCamera()
		}else {
			pickImageFromGallery()
		}
	}

	private fun pickVideo(fromCamera: Boolean) {
		if (fromCamera) {
			pickVideoFromCamera()
		}else {
			pickVideoFromGallery()
		}
	}

	private fun pickVideoFromCamera() {
		activityResultVideoCamera?.launchSafely(
			null,
			Intent(MediaStore.ACTION_VIDEO_CAPTURE).also {
				it.putExtra(MediaStore.EXTRA_DURATION_LIMIT,maxVideoLengthInSeconds)
			}
		)
	}

	private fun pickVideoFromGallery() {
		activityResultVideoGallery?.launchSafely(null, Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI))
	}

	private fun pickCamera() {
		when (supportedMediaType) {
			SupportedMediaType.VIDEO -> pickVideoFromCamera()
			SupportedMediaType.IMAGE -> pickImageFromCamera()
			SupportedMediaType.BOTH -> {
				val fragment = handler.weakRefHost.get() as? Fragment ?: return

				val imageString = fragment.getString(R.string.image)
				val videoString = fragment.getString(R.string.video)

				getAnchor(tag)?.showPopup(
					listOf(imageString, videoString),
					onMenuItemClickListener = {
						when (it.title.toStringOrEmpty()) {
							imageString -> pickImageFromCamera()
							videoString -> pickVideoFromCamera()
						}
					}
				)
			}
		}
	}

	private fun pickGallery() {
		when (supportedMediaType) {
			SupportedMediaType.VIDEO -> pickVideoFromGallery()
			SupportedMediaType.IMAGE -> pickImageFromGallery()
			SupportedMediaType.BOTH -> {
				val fragment = handler.weakRefHost.get() as? Fragment ?: return

				val imageString = fragment.getString(R.string.image)
				val videoString = fragment.getString(R.string.video)

				getAnchor(tag)?.showPopup(
					listOf(imageString, videoString),
					onMenuItemClickListener = {
						when (it.title.toStringOrEmpty()) {
							imageString -> pickImageFromGallery()
							videoString -> pickVideoFromGallery()
						}
					}
				)
			}
		}
	}

	override fun onSubsetPermissionsAccepted(permissions: Map<String, Boolean>) {
		MyLogger.e("aaaaaaaaaa -> on subset $permissions")

		if (permissions[Manifest.permission.CAMERA] == true) {
			pickCamera()
		}else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
			(permissions[Manifest.permission.READ_MEDIA_IMAGES] == true || permissions[Manifest.permission.READ_MEDIA_VIDEO] == true)) {
			pickGallery()
		}else if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
			&& permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
			pickGallery()
		}
	}

	enum class SupportedMediaType {
		VIDEO, IMAGE, BOTH
	}

	enum class SourceOfData {
		Gallery, Camera, BOTH
	}

}
