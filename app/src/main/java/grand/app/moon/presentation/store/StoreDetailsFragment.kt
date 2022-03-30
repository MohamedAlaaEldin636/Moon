package grand.app.moon.presentation.store

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentAdsDetailsBinding
import grand.app.moon.databinding.FragmentStoreDetailsBinding
import grand.app.moon.presentation.ads.viewModels.AdsDetailsViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.StoreDetailsViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreDetailsFragment : BaseFragment<FragmentStoreDetailsBinding>() {

  private val adsDetailsFragmentArgs : StoreDetailsFragmentArgs by navArgs()

  private val viewModel: StoreDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_store_details

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.getDetails(adsDetailsFragmentArgs.id)
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(viewLifecycleOwner,{
      if(it == Constants.LOGIN_REQUIRED) openLoginActivity()
    })
    lifecycleScope.launchWhenResumed {
      viewModel.storeDetailsResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.update(it.value.data)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
  }

  private  val TAG = "AdsDetailsFragment"
  override fun onResume() {
    super.onResume()
    Log.d(TAG, "token: ${viewModel.userLocalUseCase.getKey(Constants.TOKEN)}")
    if(!viewModel.isLoggin) {
      val isAuthorize = viewModel.userLocalUseCase.isLoggin()
      viewModel.recallApi(isAuthorize)
    }
  }

  override fun onStart() {
    super.onStart()
//    Toast.makeText(context, "onStart", Toast.LENGTH_SHORT).show()
  }
}