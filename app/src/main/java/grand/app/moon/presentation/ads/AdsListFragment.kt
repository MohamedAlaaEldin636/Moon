package grand.app.moon.presentation.ads

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
import grand.app.moon.presentation.ads.viewModels.AdsDetailsViewModel
import grand.app.moon.presentation.ads.viewModels.AdsListViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AdsListFragment : BaseFragment<FragmentAdsListBinding>() {


  private val viewModel: AdsListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_ads_list

  private  val TAG = "AdsListFragment"

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    if(arguments?.containsKey(Constants.TYPE) == true && arguments?.getInt(Constants.TYPE) != -1){
      viewModel.ADS_LIST_URL+="type="+requireArguments().getInt(Constants.TYPE)
    }

    if(arguments?.containsKey(Constants.CATEGORY_ID) == true && arguments?.getInt(Constants.CATEGORY_ID) != -1)
        viewModel.ADS_LIST_URL+="category_id="+arguments?.getInt(Constants.CATEGORY_ID)
    if(arguments?.containsKey(Constants.SUB_CATEGORY_ID) == true && arguments?.getInt(Constants.SUB_CATEGORY_ID) != -1)
      viewModel.ADS_LIST_URL+="sub_category_id="+arguments?.getInt(Constants.SUB_CATEGORY_ID)
    if(viewModel.type != -1)
    Log.d(TAG, "setBindingVariables: ${viewModel.ADS_LIST_URL}")
    Log.d(TAG, "${viewModel.ADS_LIST_URL}")
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