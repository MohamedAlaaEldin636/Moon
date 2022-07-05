package grand.app.moon.presentation.media.image.pager

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentZoomPagerBinding
import grand.app.moon.presentation.media.image.ZoomViewModel

@AndroidEntryPoint
class ZoomPagerFragment : BaseFragment<FragmentZoomPagerBinding>() {

//  val args : ZoomPagerFragmentArgs by navArgs()
  private val viewModel: ZoomViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_zoom_pager

  override
  fun setBindingVariables() {
//    viewModel.setImages(args.images.toList())
    binding.viewModel = viewModel
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onPause() {
    super.onPause()
  }

}