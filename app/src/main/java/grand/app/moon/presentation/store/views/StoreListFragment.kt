package grand.app.moon.presentation.store.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.core.extenstions.actOnGetIfNotInitialValueOrGetLiveData
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentStoreListBinding
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.StoreListViewModel

@AndroidEntryPoint
class StoreListFragment : BaseFragment<FragmentStoreListBinding>() {

  val viewModel: StoreListViewModel by viewModels()
  val args : StoreListFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_store_list

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    if(args.orderBy != -1) viewModel.request.order_by = args.orderBy
    viewModel.request.search = args.search
    binding.appCompatEditText.setText(args.search)
    if(args.categoryId != -1)
      viewModel.request.categoryId = args.categoryId
    viewModel.callService()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Log.d(TAG, "onViewCreated: HERERE")
    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      Log.d(TAG, "onViewCreated: HERE")
//      if(bundle.containsKey(Constants.STORE_FILTER)) {
//        Log.d(TAG, "onViewCreated: THERE")
//        viewModel.reset()
//        viewModel.request = bundle.getSerializable(Constants.STORE_FILTER) as StoreFilterRequest
//        viewModel.adapter.type = 4
//        viewModel.callService()
//      }
      if(bundle.containsKey(Constants.SORT_BY)) {
        Log.d(TAG, "onViewCreated: ASDASD")
        viewModel.request.order_by = bundle.getInt(Constants.SORT_BY)
        viewModel.reset()
        viewModel.callService()
      }
    }


    actOnGetIfNotInitialValueOrGetLiveData(
      Constants.STORE_FILTER,
      FilterResultRequest(),
      viewLifecycleOwner,
      { it != null }
    ) {
      it?.let { filterRequest ->
        Log.d(TAG, "onViewCreated: ASDASDSA")
        if(filterRequest.checked) {
          Log.d(TAG, "onViewCreated: WA")
          viewModel.reset()
          viewModel.request = filterRequest
          viewModel.adapter.type = 4
          viewModel.callService()
        }
      }

    }

  }

  override
  fun setUpViews() {
    setRecyclerViewScrollListener()
    binding.appCompatEditText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
      if (i == EditorInfo.IME_ACTION_SEARCH && textView.text.trim().isNotEmpty()) {
        Log.d(TAG, "setUpViews: HEREE SEARCH")
        viewModel.reset()
        viewModel.adapter.type = 4
        viewModel.request.search =  textView.text.toString()
        viewModel.callService()
        return@OnEditorActionListener true
      }
      false
    })

  }


  private  val TAG = "StoreListFragment"
  override fun setupObservers() {

    viewModel.clickEvent.observe(this, {
      when (it) {
        Constants.GRID_1 , Constants.GRID_2 -> {
          viewModel.adapter.notifyDataSetChanged()
        }
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
      }
    }
  }

  fun setSpan(counter: Int){
    Log.d(TAG, "setSpan: $counter")
    val gridLayoutManager = GridLayoutManager(context,counter,GridLayoutManager.VERTICAL,false)
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return when(viewModel.adapter.grid){
          Constants.GRID_1 -> 2
          else -> 1
        }
      }
    }
//    Log.d(TAG, "setSpan: ${gridLayoutManager.spanCount}")
    binding.recyclerView.layoutManager = gridLayoutManager
    binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!recyclerView.canScrollVertically(1)){
          Log.d(TAG, "onScrollStateChanged: RECY")
          viewModel.callService()
        }
      }
    })
    binding.recyclerView.adapter = viewModel.adapter
  }

  private fun setRecyclerViewScrollListener() {
    setSpan(2)
  }

  override fun onResume() {
    super.onResume()
    viewModel.adapter.checkBlockStore()
    viewModel.adapter.checkFollowingStore()
    viewModel.notifyAdapters()
  }

}