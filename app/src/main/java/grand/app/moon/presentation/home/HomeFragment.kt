package grand.app.moon.presentation.home

import androidx.fragment.app.activityViewModels
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
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

  private val viewModel: HomeViewModel by viewModels()
  private val activityViewModel: HomeViewModel by activityViewModels()

  override
  fun getLayoutId() = R.layout.fragment_home

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.initAllServices()
  }

  private val TAG = "HomeFragment"

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
                ca.copy(name = "${resources.getString(R.string.advertisements)} ${ca.name}")
              })
            )
            hr.categoryAds.forEach {
              it.showMore.categoryId = it.id
            }

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
            val store = Store()
            store.stories.add(StoryItem(name = getString(R.string.show_more), isFirst = true))
            it.value.data.add(0,store )
            viewModel.updateStories(it.value.data)
          }
        }
    }
  }


  //  private fun updateList(data: HomeResponse) {
//    data.categoryAds.forEach {
//      it.name = "${resources.getString(R.string.advertisement)} ${it.name}"
//    }
//    if (data.mostPopularAds.isNotEmpty()) {
//      val categoryAdvertisement = CategoryAdvertisement()
//      categoryAdvertisement.name = resources.getString(R.string.suggestions_ads_for_you)
//      categoryAdvertisement.advertisements.addAll(data.suggestions)
//      data.categoryAds.add(0, categoryAdvertisement)
//    }
//    if (data.mostPopularAds.isNotEmpty()) {
//      val categoryAdvertisement = CategoryAdvertisement()
//      categoryAdvertisement.name = resources.getString(R.string.most_popular_ads)
//      categoryAdvertisement.advertisements.addAll(data.mostPopularAds)
//      data.categoryAds.add(0, categoryAdvertisement)
//    }
//  }
  private fun updateList(data: HomeResponse) {

    if (data.mostPopularAds.isNotEmpty()) {
      val categoryAdvertisement = CategoryAdvertisement()
      categoryAdvertisement.name = resources.getString(R.string.most_popular_ads)
      categoryAdvertisement.advertisements.addAll(data.mostPopularAds)
      data.categoryAds.add(0, categoryAdvertisement)
    }
    if (data.suggestions.isNotEmpty()) {
      val categoryAdvertisement = CategoryAdvertisement()
      categoryAdvertisement.name = resources.getString(R.string.suggestions_ads_for_you)
      categoryAdvertisement.advertisements.addAll(data.suggestions)
      data.categoryAds.add(0, categoryAdvertisement)
    }
    //hey I'm HERE
  }

}