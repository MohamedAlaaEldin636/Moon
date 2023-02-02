package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentHome2Binding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBack
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.models.ItemHomeRV
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.home.viewModels.Home2ViewModel

@AndroidEntryPoint
class Home2Fragment : BaseFragment<FragmentHome2Binding>() {

	private val viewModel by viewModels<Home2ViewModel>()

	/** todo search on click go to screen empty screen lottie else local cache of previous clicked or keyboard search clicks isa. */
	override fun getLayoutId(): Int = R.layout.fragment_home_2

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)

		// Called here to auto update in case of any change happens isa.
		getWholeHomeData()
	}

	fun getWholeHomeData(showGlobalLoadingNotSwipeAbleOne: Boolean = true) {
		handleRetryAbleActionOrGoBack(
			scope = viewLifecycleOwner.lifecycleScope,
			action = {
				viewModel.repoHome2.getWholeHomeData()
			},
			showLoadingCode = {
				if (showGlobalLoadingNotSwipeAbleOne) {
					showLoading()
				}else {
					_binding?.swipeRefreshLayout?.isRefreshing = true
				}
			},
			hideLoadingCode = {
				if (showGlobalLoadingNotSwipeAbleOne) {
					hideLoading()
				}else {
					_binding?.swipeRefreshLayout?.isRefreshing = false
				}
			}
		) { data ->
			// 1st item just used for addition isa.
			viewModel.adapterStories.submitList(listOf(ResponseStory()) + data.stories)

			viewModel.adapterCategories.submitList(data.categories)

			viewModel.adapterMostRatedStore.submitList(data.responseHome.mostRatedStores.orEmpty())

			viewModel.adapterFollowingsStores.submitList(data.responseHome.followingsStores.orEmpty())

			viewModel.adapterSuggestedAds.submitList(data.responseHome.suggestedAds.orEmpty())

			viewModel.adapterMostPopularAds.submitList(data.responseHome.mostPopularAds.orEmpty())

			val dynamicCategoriesAds = data.responseHome.dynamicCategoriesAds.orEmpty().filter {
				it.advertisements.isNullOrEmpty().not()
			}
			viewModel.adapterDynamicCategoryAds = dynamicCategoriesAds.map { category ->
				viewModel.getAdapterForAds().also { it.submitList(category.advertisements.orEmpty()) }
			}

			val list = mutableListOf(
				ItemHomeRV(ItemHomeRV.Type.STORIES, null),
				ItemHomeRV(ItemHomeRV.Type.CATEGORIES, getString(R.string.departments)),
			)

			if (viewModel.adapterMostRatedStore.itemCount > 0) {
				list += ItemHomeRV(ItemHomeRV.Type.MOST_RATED_STORIES, getString(R.string.most_rated_stores))
			}
			if (viewModel.adapterFollowingsStores.itemCount > 0) {
				list += ItemHomeRV(ItemHomeRV.Type.FOLLOWING_STORIES, getString(R.string.following_stories))
			}
			if (viewModel.adapterSuggestedAds.itemCount > 0) {
				list += ItemHomeRV(ItemHomeRV.Type.SUGGESTED_ADS, getString(R.string.suggestions_ads_for_you))
			}
			if (viewModel.adapterMostPopularAds.itemCount > 0) {
				list += ItemHomeRV(ItemHomeRV.Type.MOST_POPULAR_ADS, getString(R.string.most_popular_ads))
			}

			if (dynamicCategoriesAds.isNotEmpty()) {
				for (item in dynamicCategoriesAds) {
					list += ItemHomeRV(ItemHomeRV.Type.DYNAMIC_CATEGORIES_ADS, item.name, item.adsCount)
				}
			}

			viewModel.adapter.submitList(list)
		}
	}

}

/*
    <!--
     ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
     0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP
     ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(ListActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(ListActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                arrayList.remove(position);
                adapter.notifyDataSetChanged();

            }
     };
    -->

*
* */