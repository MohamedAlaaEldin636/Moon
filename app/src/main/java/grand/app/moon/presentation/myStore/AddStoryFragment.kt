package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.util.Log
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.grand.trim_video_lib.LoadingTrimActivity
import com.grand.trim_video_lib.MAActivityResultContracts
import com.grand.trim_video_lib.registerForActivityResultTrimVideo
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.core.extenstions.createMultipartBodyPartAsFile
import grand.app.moon.databinding.FragmentAddExploreBinding
import grand.app.moon.databinding.FragmentAddStoryBinding
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.myStore.viewModel.AddExploreViewModel
import grand.app.moon.presentation.myStore.viewModel.AddStoryViewModel

@AndroidEntryPoint
class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {

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