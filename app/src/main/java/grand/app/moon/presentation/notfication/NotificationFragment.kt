package grand.app.moon.presentation.notfication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.databinding.FragmentNotificationBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SwipeToDeleteCallback
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import grand.app.moon.presentation.notfication.viewmodel.NotificationListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

  private val activityViewModel: HomeViewModel by activityViewModels()

  private val viewModel: NotificationListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_notification

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.callService()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      if(bundle.containsKey(Constants.SORT_BY)) {
        viewModel.type = bundle.getInt(Constants.SORT_BY)
        viewModel.reset()
        viewModel.callService()
      }
    }
  }

  override
  fun setUpViews() {
    setRecyclerViewScrollListener()
    val swipeHandler = object : SwipeToDeleteCallback(requireActivity()) {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewModel.remove(viewModel.adapter.differ.currentList[viewHolder.absoluteAdapterPosition])
        viewModel.adapter.removeItem(viewHolder.absoluteAdapterPosition)
        showMessage(getString(R.string.notification_removed_successfully))
      }
    }
    val itemTouchHelper = ItemTouchHelper(swipeHandler)
    itemTouchHelper.attachToRecyclerView(binding.recyclerView)

  }


  override fun setupObservers() {

    activityViewModel.clickEvent.observe(this,{
      if(it == Constants.NOTIFICATION_FILTER){
        findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToFilterSortDialog(viewModel.type,FilterDialog.NOTIFICATION))
      }
    })
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
        println("collection here $it")
        if(it is Resource.Success){
          viewModel.setData(it.value.data)
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