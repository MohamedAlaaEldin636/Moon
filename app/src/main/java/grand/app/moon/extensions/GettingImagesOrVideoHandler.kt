package grand.app.moon.extensions

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
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.presentation.myAds.addAdvFinalPage.CameraUtils

/*fun createGettingVideoHandler(
	fragment: Fragment,
	maxVideoLengthInSeconds: Int = 3 * 60,

) = GettingImagesOrVideoHandler(
	fragment,
	GettingImagesOrVideoHandler.SupportedMediaType.VIDEO,
	maxVideoLengthInSeconds,

)*/

class PickImagesOrVideoHandler(
	fragment: Fragment,
	private val supportedMediaType: SupportedMediaType,
	private val maxVideoLengthInSeconds: Int = 3 * 60, // Ex. 3 minutes -> 3 * 60
	private val requestMultipleImages: Boolean = false,
	private val getAnchor: (tag: Bundle) -> View?,
	private val onReceive: (uris: List<Uri>, fromCamera: Boolean, isImageNotVideo: Boolean) -> Unit,
) : PermissionsHandler.Listener {

	private val handler = PermissionsHandler(
		fragment,
		fragment.lifecycle,
		fragment.requireContext(),
		buildList {
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
			add(Manifest.permission.CAMERA)
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
				intent.createChooserMA(fragment.getString(R.string.pick_image))
			)
		}else {
			val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

			activityResultImageGallery?.launchSafely(
				intent.createChooserMA(fragment.getString(R.string.pick_image))
			)
		}
	}

	override fun onAllPermissionsAccepted() {
		MyLogger.e("aaaaaaaaaa -> on all")

		val fragment = handler.weakRefHost.get() as? Fragment ?: return

		val imageCamera = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.camera)})"
		val imageGallery = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.gallery)})"

		val videoCamera = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.camera)})"
		val videoGallery = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.gallery)})"

		val cameraString = fragment.getString(R.string.camera)
		val galleryString = fragment.getString(R.string.gallery)

		val list = mutableListOf<String>()

		when(supportedMediaType) {
			SupportedMediaType.IMAGE, SupportedMediaType.VIDEO -> {
				list += cameraString
				list += galleryString
			}
			SupportedMediaType.BOTH -> {
				list += imageCamera
				list += imageGallery

				list += videoCamera
				list += videoGallery
			}
		}

		MyLogger.e("aaaaaaaaaa -> ${getAnchor(tag) == null} $list")

		getAnchor(tag)?.showPopup(
			list,
			listener = {
				when (val title = it.title.toStringOrEmpty()) {
					imageCamera, imageGallery -> pickImage(title == imageCamera)
					videoCamera, videoGallery -> pickVideo(title == videoCamera)
					cameraString -> if (supportedMediaType == SupportedMediaType.IMAGE) {
						pickImageFromCamera()
					}else {
						pickVideoFromCamera()
					}
					galleryString -> if (supportedMediaType == SupportedMediaType.IMAGE) {
						pickImageFromGallery()
					}else {
						pickVideoFromGallery()
					}
				}
			}
		)
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
		activityResultVideoCamera?.launchSafely(Intent(MediaStore.ACTION_VIDEO_CAPTURE).also {
			it.putExtra(MediaStore.EXTRA_DURATION_LIMIT,maxVideoLengthInSeconds)
		})
	}

	private fun pickVideoFromGallery() {
		activityResultVideoGallery?.launchSafely(Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI))
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
					listener = {
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
					listener = {
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

}



/*
class GettingImagesOrVideoHandler(
	fragment: Fragment,
	private val supportedMediaType: SupportedMediaType,
	private val maxVideoLengthInSeconds: Int = 3 * 60, // Ex. 3 minutes -> 3 * 60
	private val requestMultipleImages: Boolean = false,
	private val getAnchor: (tag: Bundle) -> View?,
	private val onReceive: (uris: List<Uri>, fromCamera: Boolean, isImageNotVideo: Boolean) -> Unit,
) : PermissionsHandler(
	fragment,
	fragment.lifecycle,
	fragment.requireContext(),
	buildList {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			when (supportedMediaType) {
				SupportedMediaType.IMAGE -> add(Manifest.permission.READ_MEDIA_IMAGES)
				SupportedMediaType.VIDEO -> add(Manifest.permission.READ_MEDIA_VIDEO)
				SupportedMediaType.BOTH -> {
					add(Manifest.permission.READ_MEDIA_VIDEO)
					add(Manifest.permission.READ_MEDIA_VIDEO)
				}
			}
		}else {
			add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			add(Manifest.permission.READ_EXTERNAL_STORAGE)
		}
		add(Manifest.permission.CAMERA)
	},
	object : Listener {
		override fun onAllPermissionsAccepted() {
			TODO("Not yet implemented")
		}
	}
) {

	private var tag = Bundle.EMPTY

	private val activityResultVideoCamera = if (supportedMediaType == SupportedMediaType.IMAGE) null else fragment.registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		if (it.resultCode == Activity.RESULT_OK) {
			val uri = it.data?.data ?: return@registerForActivityResult

			val context = (weakRefHost.get() as? Fragment)?.context ?: return@registerForActivityResult

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

			val context = (weakRefHost.get() as? Fragment)?.context ?: return@registerForActivityResult

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

		actOnAllPermissionsAcceptedOrRequestPermissions()
	}

	private fun pickImageFromCamera() {
		val fragment = weakRefHost.get() as? Fragment ?: return

		activityResultImageCameraFile?.launchSafely(
			CameraUtils.createImageUri(fragment.activity ?: return)
		)
	}

	private fun pickImageFromGallery() {
		val fragment = weakRefHost.get() as? Fragment ?: return

		if (requestMultipleImages) {
			val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
				it.type = "image/*"
				it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
			}

			activityResultImageGallery?.launchSafely(
				intent.createChooserMA(fragment.getString(R.string.pick_image))
			)
		}else {
			val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

			activityResultImageGallery?.launchSafely(
				intent.createChooserMA(fragment.getString(R.string.pick_image))
			)
		}
	}

	private inner class AutoListener : Listener {

		override fun onAllPermissionsAccepted() {
			MyLogger.e("aaaaaaaaaa -> on all")

			val fragment = weakRefHost.get() as? Fragment ?: return

			val imageCamera = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.camera)})"
			val imageGallery = "${fragment.getString(R.string.image)} (${fragment.getString(R.string.gallery)})"

			val videoCamera = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.camera)})"
			val videoGallery = "${fragment.getString(R.string.video)} (${fragment.getString(R.string.gallery)})"

			val cameraString = fragment.getString(R.string.camera)
			val galleryString = fragment.getString(R.string.gallery)

			val list = mutableListOf<String>()

			when(supportedMediaType) {
				SupportedMediaType.IMAGE, SupportedMediaType.VIDEO -> {
					list += cameraString
					list += galleryString
				}
				SupportedMediaType.BOTH -> {
					list += imageCamera
					list += imageGallery

					list += videoCamera
					list += videoGallery
				}
			}

			MyLogger.e("aaaaaaaaaa -> ${getAnchor(tag) == null} $list")

			getAnchor(tag)?.showPopup(
				list,
				listener = {
					when (val title = it.title.toStringOrEmpty()) {
						imageCamera, imageGallery -> pickImage(title == imageCamera)
						videoCamera, videoGallery -> pickVideo(title == videoCamera)
						cameraString -> if (supportedMediaType == SupportedMediaType.IMAGE) {
							pickImageFromCamera()
						}else {
							pickVideoFromCamera()
						}
						galleryString -> if (supportedMediaType == SupportedMediaType.IMAGE) {
							pickImageFromGallery()
						}else {
							pickVideoFromGallery()
						}
					}
				}
			)
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
			activityResultVideoCamera?.launchSafely(Intent(MediaStore.ACTION_VIDEO_CAPTURE).also {
				it.putExtra(MediaStore.EXTRA_DURATION_LIMIT,maxVideoLengthInSeconds)
			})
		}

		private fun pickVideoFromGallery() {
			activityResultVideoGallery?.launchSafely(Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI))
		}

		private fun pickCamera() {
			when (supportedMediaType) {
				SupportedMediaType.VIDEO -> pickVideoFromCamera()
				SupportedMediaType.IMAGE -> pickImageFromCamera()
				SupportedMediaType.BOTH -> {
					val fragment = weakRefHost.get() as? Fragment ?: return

					val imageString = fragment.getString(R.string.image)
					val videoString = fragment.getString(R.string.video)

					getAnchor(tag)?.showPopup(
						listOf(imageString, videoString),
						listener = {
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
				SupportedMediaType.VIDEO -> pickVideoFromCamera()
				SupportedMediaType.IMAGE -> pickImageFromGallery()
				SupportedMediaType.BOTH -> {
					val fragment = weakRefHost.get() as? Fragment ?: return

					val imageString = fragment.getString(R.string.image)
					val videoString = fragment.getString(R.string.video)

					getAnchor(tag)?.showPopup(
						listOf(imageString, videoString),
						listener = {
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

	}

	enum class SupportedMediaType {
		VIDEO, IMAGE, BOTH
	}

}*/
 */
