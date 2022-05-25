package grand.app.moon.presentation.ads

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentAdsDetailsBinding
import grand.app.moon.domain.home.models.review.ReviewsPaginateData
import grand.app.moon.presentation.ads.viewModels.AdsDetailsViewModel
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AdsDetailsFragment : BaseFragment<FragmentAdsDetailsBinding>() {

  private val adsDetailsFragmentArgs : AdsDetailsFragmentArgs by navArgs()

  val viewModel: AdsDetailsViewModel by viewModels()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
//      // We use a String here, but any type that can be put in a Bundle is supported
////      val result = bundle.getSerializable(Constants.REVIEWS_RESPONSE) as ReviewsPaginateData
////      viewModel.updateReviews(result)
////      viewModel.advertisement.get()!!.averageRate = bundle.getString(Constants.RATE).toString()
//      // Do something with the result
//
//      Log.d(TAG, "onCreate: HERE")
//      if (bundle.containsKey(Constants.STORES_ID) && (bundle.containsKey(Constants.STORES_FOLLOWED) || bundle.containsKey(
//          Constants.STORES_BLOCKED
//        ))
//      ) {
//        Log.d(TAG, "onCreate: FAVOURITE")
//        if (bundle.containsKey(Constants.STORES_FOLLOWED))
//          viewModel.advertisement.get()?.store?.isFollowing = bundle.getBoolean(Constants.STORES_FOLLOWED)
//        if (bundle.containsKey(Constants.STORES_BLOCKED)){
//          viewModel.blockStore = true
//          backToPreviousScreen()
//        }
//      }
    }
  }

  override
  fun getLayoutId() = R.layout.fragment_ads_details

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.getDetails(adsDetailsFragmentArgs.id,adsDetailsFragmentArgs.type,adsDetailsFragmentArgs.fromStore)

  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observe(viewLifecycleOwner,{
      if(it == Constants.LOGIN_REQUIRED) openLoginActivity()
      if(it == Constants.SHARE_IMAGE) viewModel.share(binding.imgShare)
    })
    lifecycleScope.launchWhenResumed {
      viewModel.adsDetailsResponse.collect {
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
    viewModel.adsAdapter.clickEvent.observe(viewLifecycleOwner,{

    })
  }

  private  val TAG = "AdsDetailsFragment"
  override fun onResume() {
    super.onResume()
    viewModel.adsAdapter.updateFavourite()
//    Log.d(TAG, "token: ${viewModel.userLocalUseCase.getKey(Constants.TOKEN)}")
//    if(!viewModel.isLoggin) {
//      val isAuthorize = viewModel.userLocalUseCase.isLoggin()
//      viewModel.recallApi(isAuthorize)
//    }else
  }

  override fun onStart() {
    super.onStart()
//    Toast.makeText(context, "onStart", Toast.LENGTH_SHORT).show()
  }

  override fun onDestroy() {
    viewModel.advertisement.get()?.let {
      Log.d(TAG, "onDestroy: ADVERTISEMENT")
      val bundle = Bundle()
      bundle.putInt(Constants.ID,it.id)
      bundle.putBoolean(Constants.FAVOURITE,it.isFavorite)
      it.store?.id?.let { it1 -> bundle.putInt(Constants.STORES_ID, it1) }
      it.store?.isFollowing?.let { it1 -> bundle.putBoolean(Constants.STORES_FOLLOWED, it1) }
      bundle.putBoolean(Constants.STORES_BLOCKED,viewModel.blockStore)
      setFragmentResult(Constants.BUNDLE, bundle)
    }
    super.onDestroy()
  }

}