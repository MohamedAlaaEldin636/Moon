package grand.app.moon.presentation.discover

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentDiscoverBinding
import grand.app.moon.databinding.FragmentMyAccountBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.more.MoreItem
import kotlinx.coroutines.flow.collect
import java.util.ArrayList

@AndroidEntryPoint
class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {


  private val viewModel: DiscoverViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_discover

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
//    viewModel.moreAdapter.differ.submitList(arrayListOf())
    lifecycleScope.launchWhenCreated {
      viewModel.response.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setData(it.value.data)

          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
	        else -> {}
        }
      }
    }

    viewModel.adapter.clickEvent.observe(this, {
      Log.d(TAG, "setBindingVariables: $it")
      if (it != -1) {
        findNavController().navigate(DiscoverFragmentDirections.actionDiscoverFragmentToNavExplore())
      }
    })
  }

  override fun onStop() {
    super.onStop()
    Log.d(TAG, "onStop: ")
  }

  override fun onPause() {
    super.onPause()
    viewModel.adapter.clickEvent.value = -1
  }

  private val TAG = "MoreFragment"

}