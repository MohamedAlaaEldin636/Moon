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
import grand.app.moon.databinding.FragmentStoreUsersBinding
import grand.app.moon.databinding.FragmentUserListBinding
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.StoreUsersViewModel
import grand.app.moon.presentation.user.viewmodel.UserListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreUsersFragment : BaseFragment<FragmentStoreUsersBinding>() {

  val args: StoreUsersFragmentArgs by navArgs()
  private val viewModel: StoreUsersViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_store_users

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.storeId = args.storeId
    viewModel.type = args.type
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