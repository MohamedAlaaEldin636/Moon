package grand.app.moon.presentation.myStore

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import com.grand.trim_video_lib.MAActivityResultContracts
import com.grand.trim_video_lib.TrimmingVideoNum2Activity
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.core.extenstions.createMultipartBodyPartAsFile
import grand.app.moon.databinding.FragmentAddStoryBinding
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.AddStoryViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {

	fun Uri.changeFileMimeType(context: Context, newMimeType: String): Uri? {
		val uri = this
		val contentValues = ContentValues().apply {
			put(MediaStore.MediaColumns.MIME_TYPE, newMimeType)
		}
		val rowsUpdated = context.contentResolver.update(uri, contentValues, null, null)
		if ((rowsUpdated > 0).not()) return null

		return this
	} // todo ...

	fun File.renameFile(newFileName: String): File? {
		val oldFile = this
		if (!oldFile.exists()) {
			// File does not exist
			return null
		}
		val newFile = File(oldFile.parent, newFileName)
		if (oldFile.renameTo(newFile).not()) return null

		return newFile
	}

	fun copyFileSchemeUriToContentSchemeUri(context: Context, fileUri: Uri): Uri? {
		try {
			val inputStream = context.contentResolver.openInputStream(fileUri)
			inputStream?.let { inputStream ->
				val outputUri = createNewContentSchemeUri(context)
				val outputStream = context.contentResolver.openOutputStream(outputUri)
				outputStream?.let {
					val buffer = ByteArray(4096)
					var len = 0
					while (inputStream.read(buffer).also { len = it } > 0) {
						outputStream.write(buffer, 0, len)
					}
					return outputUri
				}
			}
		} catch (e: Exception) {
			Log.e("TAG", "Error copying file scheme Uri to content scheme Uri", e)
		}
		return null
	}

	private fun createNewContentSchemeUri(context: Context): Uri {
		val resolver = context.contentResolver
		val contentValues = ContentValues().apply {
			put(MediaStore.MediaColumns.DISPLAY_NAME, "temp_file")
			put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4"/*"application/octet-stream"*/)
		}
		return resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)!!
	}

	private fun Uri.getFileFromUriiii(): File {
		val contentResolver = requireContext().contentResolver;
		val inputStream = contentResolver.openInputStream(this)
		val file = File(requireContext().cacheDir, "temp_file")
		copyInputStreamToFile(inputStream!!, file)
		return file
	}

	private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
		FileOutputStream(file).use { outputStream ->
			val buffer = ByteArray(4096)
			var length: Int
			while (inputStream.read(buffer).also { length = it } > 0) {
				outputStream.write(buffer, 0, length)
			}
		}
	}

	val launcherTrimVideo3 = registerForActivityResult(
		TrimmingVideoNum2Activity.StartTrim()
	) { uri ->
		MyLogger.e("${TrimmingVideoNum2Activity::class.java.name} -> hereeeeeeeee -> $uri")
		///data/user/0/grand.app.moon/filesMP4_20230218_203853.mp4

		// /data/user/0/grand.app.moon/filesMP4_20230218_201255.mp4 -> uri
		// content://grand.app.moon.fileprovider/camera_photos/P4_20230218_201255.mp4 -> contentUri
		// content://media/external/video/media/1000021096 -> shaghhaaal
		//.FileNotFoundException: /camera_photos/P4_20230218_201152.mp4: open failed: ENOENT (No such file or directory)

		val file = uri?.createMultipartBodyPart(viewModel.app, "file")
		val file2 = uri?.createMultipartBodyPartAsFile(viewModel.app, "file")
		val contentUri = kotlin.runCatching {
			FileProvider.getUriForFile(requireContext(), "grand.app.moon.fileprovider", /*uri!!.getFileFromUriiii()*/File(uri?.path.orEmpty()))
		}.getOrElse {
			MyLogger.e("diaosjdoaisjoajds ERRRRRRRRRRRR $it")
			null
		}

		val u1 = kotlin.runCatching { Uri.fromFile(File(uri?.path.orEmpty())) }.getOrNull()
		val u2 = kotlin.runCatching { Uri.parse(File(uri?.path.orEmpty()).path) }.getOrNull()
		val u3 = kotlin.runCatching { Uri.parse(uri?.path.orEmpty()) }.getOrNull()
		val u4 = kotlin.runCatching { Uri.parse(TrimmingVideoNum2Activity.aaaaaaaaaaaaa(requireContext(), uri!!)) }.getOrNull()
		val u5 = kotlin.runCatching { TrimmingVideoNum2Activity.aaaaaaaaaaaaa(requireContext(), uri!!) }.getOrNull()
		val u6 = kotlin.runCatching { Uri.fromFile(File(uri?.path.orEmpty()).renameFile("ss.mp4")) }.getOrNull()

		MyLogger.e("diaosjdoaisjoajds uuuuuuuuuuuuuuuuu $u1")
		MyLogger.e("diaosjdoaisjoajds uuuuuuuuuuuuuuuuu $u2")
		MyLogger.e("diaosjdoaisjoajds uuuuuuuuuuuuuuuuu $u3")
		MyLogger.e("diaosjdoaisjoajds uuuuuuuuuuuuuuuuu $u4")
		MyLogger.e("diaosjdoaisjoajds uuuuuuuuuuuuuuuuu $u5")
		MyLogger.e("diaosjdoaisjoajds uuuuuuuuuuuuuuuuu $u6")
		/*
			// content://media/external/video/media/1000021096 -> shaghhaaal

	/data/user/0/grand.app.moon/filesMP4_20230218_203853.mp4
content://grand.app.moon.fileprovider/camera_photos/P4_20230218_203853.mp4 false true
		 */
		val file3 = /*contentUri*/u1?.createMultipartBodyPart(viewModel.app, "file")
		val file4 = /*contentUri*/u1?.createMultipartBodyPartAsFile(viewModel.app, "file")
		MyLogger.e("diaosjdoaisjoajds ${file != null} ${file2 != null} $contentUri ${file3 != null} ${file4 != null}")
		MyLogger.e("diaosjdoaisjoajds $file4 $contentUri $file2")

		val uri9 = kotlin.runCatching {
			copyFileSchemeUriToContentSchemeUri(requireContext(), u1!!)
		}.getOrNull()
		val file9_1 = uri9?.createMultipartBodyPart(viewModel.app, "file")
		val file9_2 = uri9?.createMultipartBodyPartAsFile(viewModel.app, "file")
		val uri6 = kotlin.runCatching {
			copyFileSchemeUriToContentSchemeUri(requireContext(), u6!!)
		}.getOrNull()
		val file6_1 = uri6?.createMultipartBodyPart(viewModel.app, "file")
		val file6_2 = uri6?.createMultipartBodyPartAsFile(viewModel.app, "file")
		MyLogger.e("diaosjdoaisjoajds $uri9 $file9_1 $file9_2 ${copyFileSchemeUriToContentSchemeUri(requireContext(), uri!!)} " +
			"\n ${kotlin.runCatching { copyFileSchemeUriToContentSchemeUri(requireContext(), u6!!) }.getOrElse { it }}\n" +
			"$uri6\n" +
			"$file6_1\n" +
			"$file6_2")

		val file5 = contentUri?.createMultipartBodyPart(viewModel.app, "file")
		val file6 = contentUri?.createMultipartBodyPartAsFile(viewModel.app, "file")

		val file7 = u6?.createMultipartBodyPart(viewModel.app, "file")
		val file8 = u6?.createMultipartBodyPartAsFile(viewModel.app, "file")

		binding.root.postDelayed(250) {
			if (file8/*file6_1*/ != null) {
				viewModel.addStoryImmediately(
					this,
					file8/*file6_1*/,
					true
				)
			} // todo in splash start getting ads and after getting stories in home check ads in sync with retrieving one isa.
		}
	}

	val launcherVideoTrimmer2 = registerForActivityResult(
		MAActivityResultContracts.StartTrim()
	) { uri ->
		//(activity as? HomeActivity)?.makeHugeChanges()
		//launchSafelyTrimVideo
	}

	private val viewModel by viewModels<AddStoryViewModel>()

	var gettingImagesOrVideoHandler: PickImagesOrVideoHandler? = null
		private set

	var gettingImageHandler: PickImagesOrVideoHandler? = null
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		gettingImagesOrVideoHandler = PickImagesOrVideoHandler(
			this,
			PickImagesOrVideoHandler.SupportedMediaType.BOTH,
			3 * 60,
			false,
			getAnchor = { _binding?.buttonTextView }
		) { uris, _, isImageNotVideo ->
			if (isImageNotVideo) {
				viewModel.file.value = uris.firstOrNull()?.let { MAImagesOrVideo.Images(listOf(it)) }
			}else {
				viewModel.file.value = uris.firstOrNull()?.let { MAImagesOrVideo.Video(it) }
			}
		}

		gettingImageHandler = PickImagesOrVideoHandler(
			this,
			PickImagesOrVideoHandler.SupportedMediaType.IMAGE,
			requestMultipleImages = false,
			getAnchor = { _binding?.coverImageTextView }
		) { uris, _, isImageNotVideo ->
			if (isImageNotVideo) {
				viewModel.coverImage.value = uris.firstOrNull()
			}
		}

		super.onCreate(savedInstanceState)
	}

	override fun getLayoutId(): Int = R.layout.fragment_add_story

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}