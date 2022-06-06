package grand.app.moon.presentation.storeFilter.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.auth.countries.viewModels.CountriesViewModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentStoreFilterBinding
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.splash.SplashActivity
import grand.app.moon.presentation.storeFilter.viewModels.StoreFilterViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreFilterFragment : BaseFragment<FragmentStoreFilterBinding>() {

  private val viewModel: StoreFilterViewModel by viewModels()
  val args: StoreFilterFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_store_filter

  override
  fun setBindingVariables() {
    Log.d(TAG, "setBindingVariables: ")
    viewModel.args = args
    binding.viewModel = viewModel
    viewModel.init()
  }

  private  val TAG = "StoreFilterFragment"
  override
  fun setupObservers() {
    Log.d(TAG, "setupObservers: starting")
    viewModel.clickEvent.observe(viewLifecycleOwner,{
      Log.d(TAG, "setupObservers: WORKED")
      if(it == Constants.CONFIRM){
        Log.d(TAG, "setupObservers: CONFIRM")
        viewModel.args?.let {
          Log.d(TAG, "setupObservers: LET")
          val bundle = Bundle()
          it.storeFilter.city_ids = ArrayList(viewModel.cityAdapter.selected)
          it.storeFilter.category_ids = ArrayList(viewModel.categoriesAdapter.selected)
          bundle.putSerializable(Constants.STORE_FILTER,it.storeFilter)
          setFragmentResult(Constants.BUNDLE, bundle)
        }
        backToPreviousScreen()
      }
    })
  }

}