package grand.app.moon.presentation.explore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentExploreListBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.viewmodel.ExploreListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ExploreListFragment : BaseFragment<FragmentExploreListBinding>() {

  val exploreListFragmentArgs: ExploreListFragmentArgs by navArgs()
  val viewModel: ExploreListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_explore_list

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.page = exploreListFragmentArgs.page
    viewModel.setData(exploreListFragmentArgs.data)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      // We use a String here, but any type that can be put in a Bundle is supported
      if(bundle.containsKey(Constants.TOTAL)) {
        val result = bundle.getInt(Constants.TOTAL)
        viewModel.adapter.updateTotalComments(result)
      }
      // Do something with the result
    }
  }

  override
  fun setUpViews() {
  }

  private val TAG = "ExploreListFragment"

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

    viewModel.clickEvent.observe(this, {
      if (it == Constants.LOGIN_REQUIRED) openLoginActivity()
    })

  }
  fun share(){
    viewModel.adapter.differ.currentList[viewModel.adapter.position].shares++
    viewModel.adapter.notifyItemChanged(viewModel.adapter.position)
    context?.let { it1 -> share(it1,resources.getString(R.string.app_name),resources.getString(R.string.moon_info)+"\n"+viewModel.adapter.differ.currentList[viewModel.adapter.position].file) }
  }

  override fun onResume() {
    super.onResume()
    viewModel.adapter.user = viewModel.userLocalUseCase.invoke()
    viewModel.adapter.notifyDataSetChanged()
  }
}