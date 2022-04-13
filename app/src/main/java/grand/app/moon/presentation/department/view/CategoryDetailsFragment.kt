package grand.app.moon.presentation.department.view

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
import grand.app.moon.presentation.department.viewModels.CategoryDetailsViewModel
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
      it.name = "${resources.getString(R.string.advertisement)} ${it.name}"
    }
    //hey I'm HERE
  }

}