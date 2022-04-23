package grand.app.moon.presentation.filter.result

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentAdsDetailsBinding
import grand.app.moon.databinding.FragmentAdsListBinding
import grand.app.moon.databinding.FragmentFilterResultsBinding
import grand.app.moon.presentation.ads.viewModels.AdsDetailsViewModel
import grand.app.moon.presentation.ads.viewModels.AdsListViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilterResultsFragment : BaseFragment<FragmentFilterResultsBinding>() {

  val args: FilterResultsFragmentArgs by navArgs()
  private val viewModel: FilterResultsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_filter_results

  private  val TAG = "FilterResultsFragment"

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.request = args.filterRequest
    viewModel.callService()
  }

  override
  fun setUpViews() {
    setRecyclerViewScrollListener()
  }


  override fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel.response.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setData(it.value.data)
            Log.d(TAG, "setupObservers: ${it.value.data.list.size}")

          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }

    }
  }


  private fun setRecyclerViewScrollListener() {

    val layoutManager = LinearLayoutManager(requireContext())
    binding.recyclerView.layoutManager = layoutManager
    binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!recyclerView.canScrollVertically(1)){
          viewModel.callService()
        }
      }
    })
  }
}