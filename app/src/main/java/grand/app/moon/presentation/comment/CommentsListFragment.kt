package grand.app.moon.presentation.comment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentCommentsBinding
import grand.app.moon.databinding.FragmentExploreListBinding
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.comment.viewmodel.CommentListViewModel
//import grand.app.moon.presentation.explore.ExploreListFragmentArgs
import grand.app.moon.presentation.explore.viewmodel.ExploreListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CommentsListFragment : BaseFragment<FragmentCommentsBinding>() {

  val exploreListFragmentArgs: CommentsListFragmentArgs by navArgs()
  private val viewModel: CommentListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_comments

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.exploreId = exploreListFragmentArgs.exploreId
    viewModel.total.set(exploreListFragmentArgs.totalComments.toString())
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
    lifecycleScope.launchWhenResumed {
      viewModel._responseSend.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            val comment = it.value.data as Comment
            viewModel.adapter.add(comment)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }

    viewModel.clickEvent.observe(this, {
      if (it == Constants.LOGIN_REQUIRED) openLoginActivity()
    })

//    viewModel.adapter.clickEvent.observe(viewLifecycleOwner,{
//      when(it){
//        Constants.DELETE -> viewModel.fav()
//      }
//    })

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