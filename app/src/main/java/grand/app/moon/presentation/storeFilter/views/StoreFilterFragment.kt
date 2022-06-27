package grand.app.moon.presentation.storeFilter.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.auth.countries.viewModels.CountriesViewModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentStoreFilterBinding
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.store.entity.StoreFilterRequest
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.FilterFragmentDirections
import grand.app.moon.presentation.splash.SplashActivity
import grand.app.moon.presentation.storeFilter.viewModels.StoreFilterViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreFilterFragment : BaseFragment<FragmentStoreFilterBinding>() {

  private val viewModel: StoreFilterViewModel by viewModels()
//  val args: StoreFilterFragmentArgs by navArgs()


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
  fun getLayoutId() = R.layout.fragment_store_filter

  override
  fun setBindingVariables() {
    Log.d(TAG, "setBindingVariables: ")
//    viewModel.args = args
    binding.viewModel = viewModel
  }

  private val TAG = "StoreFilterFragment"

  override
  fun setupObservers() {
    setupAdapter()
    Log.d(TAG, "setupObservers: starting")
    viewModel.clickEvent.observe(viewLifecycleOwner, {
      Log.d(TAG, "setupObservers: WORKED")
      if (it == Constants.CONFIRM) {
        Log.d(TAG, "setupObservers: CONFIRM")
//        val bundle = Bundle()
//        bundle.putSerializable(Constants.STORE_FILTER, viewModel.request)
//        setFragmentResult(Constants.BUNDLE, bundle)
//        backToPreviousScreen()


        val n = findNavController()
        n.navigateUp()
        n.currentBackStackEntry?.savedStateHandle?.set(
          Constants.STORE_FILTER,
          viewModel.request
        )


      }
    })
  }

  private fun setupAdapter() {
    viewModel.adapter.clickEvent.observe(this, {
      Log.d(TAG, "setupObservers: ${it.filterType}")
      when (it.filterType) {
        FILTER_TYPE.CATEGORY -> {
          findNavController().navigate(
            StoreFilterFragmentDirections.actionStoreFilterFragmentToFilterSingleSelectDialog(
              it,
              it.name,
            )
          )
        }
        FILTER_TYPE.SUB_CATEGORY -> {
          when {
            viewModel.request.categoryId != null -> viewModel.request.categoryId?.let { categoryId ->
              findNavController().navigate(
                StoreFilterFragmentDirections.actionStoreFilterFragmentToFilterSingleSelectDialog(
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
        FILTER_TYPE.SINGLE_SELECT, FILTER_TYPE.CITY, FILTER_TYPE.SORT_BY, FILTER_TYPE.OTHER_OPTIONS ->
          findNavController().navigate(

            StoreFilterFragmentDirections.actionStoreFilterFragmentToFilterSingleSelectDialog(
              it,
              it.name
            )
          )
        FILTER_TYPE.AREA -> {
          when {
            (viewModel.request.cityIds != null && viewModel.request.cityIds!!.isNotEmpty()) -> {
              findNavController().navigate(
                StoreFilterFragmentDirections.actionStoreFilterFragmentToFilterMultiSelectDialog(
                  it,
                  it.name
                )
              )
            }
            else -> {
              showMessage(getString(R.string.please_choose_your_city))
            }
          }
        }
        FILTER_TYPE.MULTI_SELECT -> {
//          Log.d(TAG, "setupObservers: HERE ${it.name} , ${it.children.size}")
          findNavController().navigate(
            StoreFilterFragmentDirections.actionStoreFilterFragmentToFilterMultiSelectDialog(
              it,
              it.name
            )
          )
        }
      }
    })
  }

}