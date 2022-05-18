package grand.app.moon.presentation.category.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentCategoryDetailsBinding
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.category.viewModels.CategoryDetailsViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryDetailsFragment : BaseFragment<FragmentCategoryDetailsBinding>() {

  private val args : CategoryDetailsFragmentArgs by navArgs()
  private val viewModel: CategoryDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_category_details

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.categoryId = args.categoryId
    viewModel.title.set(args.tabBarText + " "+resources.getString(R.string.stores))
    viewModel.initAllServices()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      if(bundle.containsKey(Constants.ID) && bundle.containsKey(Constants.FAVOURITE)) {
        Log.d(TAG, "onCreate: FAVOURITE")
        viewModel.adsHomeAdapter.updateFavourite(bundle.getInt(Constants.ID),bundle.getBoolean(Constants.FAVOURITE))
      }
    }
  }


  private val TAG = "DepartmentDetailsFragment"

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
            val categoryAdvertisement = CategoryAdvertisement()
            categoryAdvertisement.id = viewModel.categoryId
            categoryAdvertisement.name = resources.getString(R.string.special_ads)
            categoryAdvertisement.advertisements.addAll(it.value.data.advertisements)

            viewModel.categoryItem.name = resources.getString(R.string.see_all_advertisement)
            viewModel.categoryItem.total = it.value.data.adsCount
            viewModel.setData(it.value.data,categoryAdvertisement)
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
            if(it.value.data.size > 0) {
              viewModel.storiesAdapter.storiesPaginate.list.addAll(it.value.data)
              val store = Store()
              store.stories.add(StoryItem(name = getString(R.string.show_more), isFirst = true))
              it.value.data.add(0, store)
              viewModel.updateStories(it.value.data)
            }
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
    data.categoryAds.forEach {
      it.name = "${resources.getString(R.string.advertisements)} ${it.name}"
    }
    //hey I'm HERE
  }

}