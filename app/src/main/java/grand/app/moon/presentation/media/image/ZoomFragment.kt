package grand.app.moon.presentation.media.image

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentDiscoverBinding
import grand.app.moon.databinding.FragmentZoomBinding
import grand.app.moon.presentation.discover.DiscoverViewModel

@AndroidEntryPoint
class ZoomFragment : BaseFragment<FragmentZoomBinding>() {

  val zoomFragmentArgs : ZoomFragmentArgs by navArgs()
  private val viewModel: ZoomViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_zoom

  override
  fun setBindingVariables() {
    viewModel.image.set(zoomFragmentArgs.image)
    binding.viewModel = viewModel
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onPause() {
    super.onPause()
  }

}