package app.grand.tafwak.presentation.my_groups

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentMyGroupDetailsBinding
import app.grand.tafwak.presentation.my_groups.viewModels.MyGroupDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGroupDetailsFragment : BaseFragment<FragmentMyGroupDetailsBinding>() {

  private val viewModel: MyGroupDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_my_group_details

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.settings)
//    binding.includedToolbar.backIv.hide()
  }

  override fun setupObservers() {
//    viewModel.clickEvent.observeForever {
//      if (it == Constants.REVIEWS)
//
//    }
  }
}