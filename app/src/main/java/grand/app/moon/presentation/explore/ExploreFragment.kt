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
import grand.app.moon.domain.utils.SpannedGridLayoutManager
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.viewmodel.ExploreViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.collections.ArrayList

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
      Collections.swap(viewModel.lastData.list, 0, it);
      navigateSafe(ExploreFragmentDirections.actionExploreFragmentToExploreListFragment(viewModel.lastData,viewModel.page))
    })
  }


  private fun setRecyclerViewScrollListener() {

    val manager = SpannedGridLayoutManager(
      object : SpannedGridLayoutManager.GridSpanLookup {
        override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
          var x = 0
          if (position % 9 == 0) {
            x = position / 9
          }

          return when {
            position == 1 || x % 2 == 1 || (position - 1) % 18 == 0 ->
              SpannedGridLayoutManager.SpanInfo(2, 2)
            else ->
              SpannedGridLayoutManager.SpanInfo(1, 1)
          }

        }
      },
      3,  // number of columns
      1f // how big is default item
    )
    binding.recyclerView.layoutManager = manager
    binding.recyclerView.adapter = viewModel.adapter


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