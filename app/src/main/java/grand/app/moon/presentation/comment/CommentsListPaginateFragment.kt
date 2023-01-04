package grand.app.moon.presentation.comment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.BR
import grand.app.moon.databinding.FragmentCommentsBinding
import grand.app.moon.databinding.FragmentExploreListBinding
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.comment.viewmodel.CommentListViewModel
//import grand.app.moon.presentation.explore.ExploreListFragmentArgs
import grand.app.moon.presentation.explore.viewmodel.ExploreListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsListPaginateFragment : BaseFragment<FragmentCommentsBinding>() {

  private val exploreListFragmentArgs: CommentsListPaginateFragmentArgs by navArgs()
  val viewModel: CommentListViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
//    binding.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//    binding.recyclerView.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()
//    down()
    viewModel.exploreId = exploreListFragmentArgs.exploreId
    viewModel.total.set(exploreListFragmentArgs.totalComments.toString())
    viewModel.callService()
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//          viewModel.response?.collectLatest {
//            viewModel.adapter.submitData(it)
//          }
      }
    }
  }

  override
  fun getLayoutId() = R.layout.fragment_comments

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
  }

  private val TAG = "CommentsListFragment"

  override fun setupObservers() {
    lifecycleScope.launchWhenResumed {
      viewModel._responseSend.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
//            viewModel.adapter.refresh()
            viewModel.total.set(viewModel.total.get()?.toInt()?.plus(1).toString())
            viewModel.clearModel()

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
      viewModel._responseDelete.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.total.set(viewModel.total.get()?.toInt()?.minus(1).toString())
//            viewModel.adapter.refresh()
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

  override fun onDestroyView() {
    setFragmentResult(Constants.BUNDLE, bundleOf(Constants.TOTAL to viewModel.total.get()?.toInt(), Constants.POSITION to exploreListFragmentArgs.position))
    super.onDestroyView()
  }
}