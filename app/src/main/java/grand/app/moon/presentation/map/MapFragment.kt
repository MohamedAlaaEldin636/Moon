package grand.app.moon.presentation.map

import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentMapBinding
import grand.app.moon.databinding.FragmentMyAccountBinding
import grand.app.moon.presentation.more.MoreItem
import java.util.ArrayList

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() {


  private val viewModel: MapViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_map

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  private val TAG = "MoreFragment"

}