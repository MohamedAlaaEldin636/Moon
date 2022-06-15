package grand.app.moon.presentation.filter.result

import android.os.Bundle
import android.util.Log
import android.view.View
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
  val viewModel: FilterResultsViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
//      if (bundle.containsKey(Constants.ID) && bundle.containsKey(Constants.FAVOURITE)) {
//        Log.d(TAG, "onCreate: FAVOURITE")
//        viewModel.adapter.updateFavourite(
//          bundle.getInt(Constants.ID),
//          bundle.getBoolean(Constants.FAVOURITE)
//        )
//      }
//      if (bundle.containsKey(Constants.STORES_ID) && (bundle.containsKey(Constants.STORES_FOLLOWED) || bundle.containsKey(
//          Constants.STORES_BLOCKED
//        ))
//      ) {
//        Log.d(TAG, "onCreate: FAVOURITE")
////        if(bundle.containsKey(Constants.STORES_FOLLOWED))
////          viewModel.storeAdapter.setFollowing(bundle.getInt(Constants.STORES_ID),bundle.getBoolean(Constants.STORES_FOLLOWED))
//        if(bundle.containsKey(Constants.STORES_BLOCKED))
//          viewModel.adapter.setBlockStore(bundle.getInt(Constants.STORES_ID))
//      }
    }
  }

  override
  fun getLayoutId() = R.layout.fragment_filter_results

  private val TAG = "FilterResultsFragment"

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.prepareRequest(args.filterRequest)
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
        if (!recyclerView.canScrollVertically(1)) {
          viewModel.callService()
        }
      }
    })
  }

  override fun onResume() {
    super.onResume()
//    viewModel.adapter.updateFavourite()
//    viewModel.adapter.checkBlockStore()
    viewModel.reset()
    viewModel.callService()
  }
}