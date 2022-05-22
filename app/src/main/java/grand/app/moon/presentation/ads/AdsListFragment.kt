package grand.app.moon.presentation.ads

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
import grand.app.moon.domain.ads.entity.AdsListPaginateData
import grand.app.moon.presentation.ads.viewModels.AdsDetailsViewModel
import grand.app.moon.presentation.ads.viewModels.AdsListViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AdsListFragment : BaseFragment<FragmentAdsListBinding>() {


  private val viewModel: AdsListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_ads_list

  private val TAG = "AdsListFragment"

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    if (arguments?.containsKey(Constants.IS_PROFILE) == true && arguments?.getBoolean(Constants.IS_PROFILE) == true) {
      viewModel.callProfilePage = requireArguments().getBoolean(Constants.IS_PROFILE)
    }
    if (arguments?.containsKey(Constants.TYPE) == true && arguments?.getInt(Constants.TYPE) != -1) {
      viewModel.type = requireArguments().getInt(Constants.TYPE)
    }
    if (arguments?.containsKey(Constants.CATEGORY_ID) == true && arguments?.getInt(Constants.CATEGORY_ID) != -1)
      viewModel.categoryId = arguments?.getInt(Constants.CATEGORY_ID)
    if (arguments?.containsKey(Constants.SUB_CATEGORY_ID) == true && arguments?.getInt(Constants.SUB_CATEGORY_ID) != -1)
      viewModel.subCateoryId = arguments?.getInt(Constants.SUB_CATEGORY_ID)
//    if (viewModel.type != -1)
    viewModel.callService()
  }

  override
  fun setUpViews() {
    setRecyclerViewScrollListener()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      if(bundle.containsKey(Constants.ID) && bundle.containsKey(Constants.FAVOURITE)) {
        Log.d(TAG, "onCreate: FAVOURITE")
        viewModel.adapter.updateFavourite(bundle.getInt(Constants.ID),bundle.getBoolean(Constants.FAVOURITE))
      }
      if(bundle.containsKey(Constants.STORES_ID) && bundle.containsKey(Constants.STORES_BLOCKED) ) {
        Log.d(TAG, "onCreate: BLOCK")
        if(bundle.containsKey(Constants.STORES_BLOCKED))
          viewModel.adapter.removeStoreAds(bundle.getInt(Constants.STORES_ID))
      }
    }
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

          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }

    }
    lifecycleScope.launchWhenResumed {
      viewModel.response_sub_category.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            val details = it.value.data
//            val list = AdsListPaginateData()
//            list.list.clear()
//            list.list.addAll(ArrayList(details.advertisements))
            viewModel.setData(details.advertisements)

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

  }
}