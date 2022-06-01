package grand.app.moon.presentation.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rizlee.rangeseekbar.RangeSeekBar
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentFilterHomeBinding
import grand.app.moon.domain.filter.entitiy.Children
import grand.app.moon.domain.filter.entitiy.FilterDetails
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.viewModels.FilterViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterHomeBinding>(), RangeSeekBar.OnRangeSeekBarPostListener  {
  private val viewModel: FilterViewModel by viewModels()

  private val TAG = "FilterHomeFragment"

  override
  fun getLayoutId() = R.layout.fragment_filter_home

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      if (bundle.containsKey(Constants.PROPERTY)) {
        viewModel.updateCallBack(bundle.getSerializable(Constants.PROPERTY) as FilterProperty)
        viewModel.submit(bundle.getSerializable(Constants.PROPERTY) as FilterProperty)
      }
    }
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    if(arguments != null){
      val bundle = arguments
      if(bundle?.getInt("category_id") != -1){
        Log.d(TAG, "setBindingVariables: HERE Category")
        viewModel.setCategoryId(bundle!!.getInt("category_id"),bundle.getString("category_name")!!)
      }
      if(bundle.getInt("sub_category_id") != -1){
        Log.d(TAG, "setBindingVariables: sub_category_id")
        viewModel.setSubCategoryId(bundle.getInt("sub_category_id"),bundle.getString("sub_category_name")!!)
      }
      if(bundle.getInt("store_id") != -1){
        Log.d(TAG, "setBindingVariables: HERE Store")
        viewModel.setStoreId(bundle.getInt("store_id"))
      }
      if(!bundle.getBoolean("allow_change_category")){
        Log.d(TAG, "setBindingVariables: allow_change_category")
        viewModel.allowChangeCategory(false)
      }
      if(bundle.containsKey(Constants.STORE)) {
        Log.d(TAG, "setBindingVariables: Storey")
        viewModel.setStore(bundle.getSerializable(Constants.STORE) as Store)
      }
    }
  }


  override fun setupObservers() {
    viewModel.adapter.clickEvent.observe(this, {
      Log.d(TAG, "setupObservers: ${it.filterType}")
      when (it.filterType) {
        FILTER_TYPE.CATEGORY -> {
          findNavController().navigate(
            FilterFragmentDirections.actionFilterFragmentToFilterSingleSelectDialog(
              it,
              it.name,
            )
          )
        }
        FILTER_TYPE.SUB_CATEGORY -> {
          when{
            viewModel.request.categoryId != null -> viewModel.request.categoryId?.let { categoryId ->
              findNavController().navigate(
                FilterFragmentDirections.actionFilterFragmentToFilterSingleSelectDialog(
                  it,
                  it.name,
                )
              )
            }
            else -> {
              showMessage(getString(R.string.please_choose_main_section))
            }
          }

        }
        FILTER_TYPE.SINGLE_SELECT, FILTER_TYPE.SORT_BY, FILTER_TYPE.OTHER_OPTIONS ->
          findNavController().navigate(

          FilterFragmentDirections.actionFilterFragmentToFilterSingleSelectDialog(
            it,
            it.name
          )
        )
        FILTER_TYPE.CITY, FILTER_TYPE.MULTI_SELECT -> {
//          Log.d(TAG, "setupObservers: HERE ${it.name} , ${it.children.size}")
          findNavController().navigate(
          FilterFragmentDirections.actionFilterFragmentToFilterMultiSelectDialog(
            it,
            it.name
          )
        )
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
            Log.d(TAG, "setupObservers_before: ${it.value.data.filterProperties.size}")
            addAdditionalFeatures(it.value.data)
            Log.d(TAG, "setupObservers_after: ${it.value.data.filterProperties.size}")
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

  private fun addAdditionalFeatures(data: FilterDetails) {
    val list = ArrayList<FilterProperty>()
    val filterProperty = FilterProperty()
    filterProperty.filterType = FILTER_TYPE.MULTI_SELECT
    filterProperty.name = getString(R.string.additional_features)
    data.filterProperties.forEach {
      if(it.type == 2){
        filterProperty.children.add(
          Children(it.id,name = it.name)
        )
      }else
        list.add(it)
    }
    if(filterProperty.children.isNotEmpty())
      list.add(filterProperty)
    data.filterProperties.clear()
    data.filterProperties.addAll(list)
  }


  override fun onStop() {
    super.onStop()
  }

  override fun onValuesChanged(minValue: Float, maxValue: Float) {
    Log.d(TAG, "onValuesChanged: sadas")
  }

  override fun onValuesChanged(minValue: Int, maxValue: Int) {
    Log.d(TAG, "onValuesChanged: asdasdassad")  }


}