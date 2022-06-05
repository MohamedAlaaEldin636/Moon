package grand.app.moon.presentation.subCategory

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentSubCategoryBinding
import grand.app.moon.domain.subCategory.entity.SubCategoryResponse
import grand.app.moon.presentation.ads.adapter.AdsHomeAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.map.MapCategoriesAdapter
import grand.app.moon.presentation.subCategory.viewModel.SubCategoryViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment<FragmentSubCategoryBinding>() {

  private val args : SubCategoryFragmentArgs by navArgs()
  val viewModel: SubCategoryViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_sub_category

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      if(bundle.containsKey(Constants.SORT_BY)) {
        viewModel.sortBy = bundle.getInt(Constants.SORT_BY)
        viewModel.reset()
        viewModel.callService()
      }
//      setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
//        if(bundle.containsKey(Constants.ID) && bundle.containsKey(Constants.FAVOURITE)) {
//          Log.d(TAG, "onCreate: FAVOURITE")
//          viewModel.adapter.updateFavourite(bundle.getInt(Constants.ID),bundle.getBoolean(Constants.FAVOURITE))
//        }
//      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    if(args.subCategory != -1) viewModel.subCategoryId = args.subCategory
    if(args.categoryId != -1) viewModel.categoryId = args.categoryId
    viewModel.type = args.type
    viewModel.isSub.set(args.isSub)
    viewModel.setCategoryId()
    viewModel.callService()
  }

  private val TAG = "SubCategoryFragment"

  override
  fun setupObservers() {
    binding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
      if (i == EditorInfo.IME_ACTION_SEARCH && textView.text.trim().isNotEmpty()) {
        viewModel.reset()
        viewModel.search = textView.text.toString()
        viewModel.callService()
        return@OnEditorActionListener true
      }
      false
    })

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
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel._responseListAds.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            val response = SubCategoryResponse()
            response.advertisements = it.value.data
            viewModel.setData(response)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }

    val gridLayoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return when(viewModel.adapter.grid){
          Constants.GRID_1 -> 2
          else -> 1
        }
      }
    }

    binding.rvAdsCategory.layoutManager = gridLayoutManager
    binding.rvAdsCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!recyclerView.canScrollVertically(1)){
          viewModel.callService()
        }
      }
    })
    binding.rvAdsCategory.adapter = viewModel.adapter

  }

  override fun onResume() {
    super.onResume()
    viewModel.adapter.updateFavourite()
    viewModel.adapter.checkBlockStore()
//    viewModel.reset()
//    viewModel.reset()
//    viewModel.callService()
  }

}