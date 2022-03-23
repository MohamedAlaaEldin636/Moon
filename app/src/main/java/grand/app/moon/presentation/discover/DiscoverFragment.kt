package grand.app.moon.presentation.discover

import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentDiscoverBinding
import grand.app.moon.databinding.FragmentMyAccountBinding
import grand.app.moon.presentation.more.MoreItem
import java.util.ArrayList

@AndroidEntryPoint
class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {


  private val viewModel: DiscoverViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_discover

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.moreAdapter.differ.submitList(arrayListOf())

  }

  private val TAG = "MoreFragment"

}