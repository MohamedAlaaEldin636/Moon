package grand.app.moon.presentation.user

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentUserListBinding
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.user.viewmodel.UserListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>() {

  val userListFragmentArgs: UserListFragmentArgs by navArgs()
  private val viewModel: UserListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_user_list

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.exploreId = userListFragmentArgs.exploreId
    viewModel.title.set(userListFragmentArgs.title)
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