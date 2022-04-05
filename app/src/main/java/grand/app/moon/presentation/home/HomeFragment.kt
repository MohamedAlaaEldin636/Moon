package grand.app.moon.presentation.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.databinding.FragmentHomeBinding
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

  private val viewModel: HomeViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_home

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.initAllServices()
  }

  private  val TAG = "HomeFragment"
  override
  fun setupObservers() {

    lifecycleScope.launchWhenResumed {
      viewModel.homeResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()


            val hr = it.value.data.copy(
              categoryAds = ArrayList(it.value.data.categoryAds.map { ca ->
                ca.copy(name = "${resources.getString(R.string.advertisement)} ${ca.name}")
              })
            )

//            updateList(hr)
            viewModel.updateList(hr)

          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.storiesResponse
        .collect {
          if (it is Resource.Success) {
            it.value.data.add(0, StoryItem(name = getString(R.string.show_more), isFirst = true))
            viewModel.updateStories(it.value.data)
          }
        }
    }
  }

  private fun updateList(data: HomeResponse) {
    data.categoryAds.forEach {
      it.name = "${resources.getString(R.string.advertisement)} ${it.name}"
    }
    if (data.mostPopularAds.isNotEmpty()) {
      val categoryAdvertisement = CategoryAdvertisement()
      categoryAdvertisement.name = resources.getString(R.string.suggestions_ads_for_you)
      categoryAdvertisement.advertisements.addAll(data.suggestions)
      data.categoryAds.add(0, categoryAdvertisement)
    }
    if (data.mostPopularAds.isNotEmpty()) {
      val categoryAdvertisement = CategoryAdvertisement()
      categoryAdvertisement.name = resources.getString(R.string.most_popular_ads)
      categoryAdvertisement.advertisements.addAll(data.mostPopularAds)
      data.categoryAds.add(0, categoryAdvertisement)
    }
    //hey I'm HERE
  }

}