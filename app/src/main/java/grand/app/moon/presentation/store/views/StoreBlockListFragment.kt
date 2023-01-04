package grand.app.moon.presentation.store.views

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.databinding.FragmentExploreListBinding
import grand.app.moon.databinding.FragmentStoreBlockListBinding
import grand.app.moon.databinding.FragmentStoreFollowedListBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.viewmodel.ExploreListViewModel
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.store.viewModels.StoreBlockListViewModel
import grand.app.moon.presentation.store.viewModels.StoreFollowedListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreBlockListFragment : BaseFragment<FragmentStoreBlockListBinding>() {

  val viewModel: StoreBlockListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_store_block_list

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
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
	        else -> {}
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.responseSubmit.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            openActivityAndClearStack(HomeActivity::class.java)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
	        else -> {}
        }
      }
    }
  }


  private fun setRecyclerViewScrollListener() {

//    val linearLayoutManager = LinearLayoutManager(context)
//    binding.recyclerView.layoutManager = linearLayoutManager
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