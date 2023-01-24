package grand.app.moon.presentation.myStore

import android.os.Bundle
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddExploreBinding
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.extensions.PickImagesOrVideoHandler
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.viewModel.AddExploreViewModel

@AndroidEntryPoint
class AddExploreFragment : BaseFragment<FragmentAddExploreBinding>() {

	private val viewModel by viewModels<AddExploreViewModel>()

	var gettingImagesOrVideoHandler: PickImagesOrVideoHandler? = null
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		gettingImagesOrVideoHandler = PickImagesOrVideoHandler(
			this,
			PickImagesOrVideoHandler.SupportedMediaType.BOTH,
			3 * 60,
			true,
			getAnchor = { _binding?.buttonTextView }
		) { uris, _, isImageNotVideo ->
			if (isImageNotVideo) {
				val amountToTake = 5

				if (uris.size > amountToTake) {
					showMessage(getString(R.string.only_var_have_been_picked, amountToTake.toString()))
				}else {
					showMessage(getString(R.string.var_have_been_picked, uris.size.toString()))
				}

				viewModel.uris.value = MAImagesOrVideo.Images(uris.take(amountToTake))
			}else {
				viewModel.uris.value = uris.firstOrNull()?.let { MAImagesOrVideo.Video(it) }
			}
		}

		super.onCreate(savedInstanceState)
	}

	override fun getLayoutId(): Int = R.layout.fragment_add_explore

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}