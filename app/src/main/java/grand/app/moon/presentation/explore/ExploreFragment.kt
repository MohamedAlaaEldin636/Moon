package grand.app.moon.presentation.explore

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentExploreBinding
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.presentation.explore.viewmodel.ExploreViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>() {

  private val viewModel: ExploreViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_explore

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.callService()
  }

  override
  fun setUpViews() {
    setRecyclerViewScrollListener()
  }

  private  val TAG = "ExploreFragment"
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

    viewModel.adapter.clickEvent.observe(this,{
      val list = ArrayList(viewModel.adapter.differ.currentList)
      viewModel.lastData.list.clear()
      viewModel.lastData.list.addAll(list)
      navigateSafe(ExploreFragmentDirections.actionExploreFragmentToExploreListFragment(viewModel.lastData,viewModel.page))
    })
  }


  private fun setRecyclerViewScrollListener() {

    val gridLayoutManager = GridLayoutManager(context,2)
    val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    binding.recyclerView.layoutManager = staggeredGridLayoutManager
//    staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
    binding.recyclerView.setHasFixedSize(true)
    binding.recyclerView.adapter = viewModel.adapter
//    binding.recyclerView.setItemViewCacheSize(binding.recyclerView.size)

    binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          viewModel.callService()
        }
      }
    })
  }
}