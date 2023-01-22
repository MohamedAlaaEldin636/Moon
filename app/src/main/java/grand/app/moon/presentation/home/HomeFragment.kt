package grand.app.moon.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.rizlee.rangeseekbar.RangeSeekBar
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.databinding.FragmentHomeBinding
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.storyView.data.Story
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), RangeSeekBar.OnRangeSeekBarPostListener , SwipeRefreshLayout.OnRefreshListener {

  val viewModel: HomeViewModel by viewModels()
  private val activityViewModel: HomeViewModel by activityViewModels()

  override
  fun getLayoutId() = R.layout.fragment_home


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {

        PrettyPopUpHelper.Builder(childFragmentManager)
          .setStyle(PrettyPopUpHelper.Style.STYLE_1_HORIZONTAL_BUTTONS)
          .setTitle(getString(R.string.logout))
          .setTitleColor(getMyColor(R.color.colorAccent))
          .setContent(getString(R.string.do_you_want_to_exit_from_app))
          .setContentColor(getMyColor(R.color.colorPrimaryDark))
          .setPositiveButtonBackground(R.drawable.corner_accent)
          .setNegativeButtonBackground(R.drawable.corner_primary)
          .setPositiveButtonTextColor(getMyColor(R.color.colorWhite))
          .setPositiveButton(getString(R.string.submit)) {
            it.dismiss()
            isEnabled = false
            activity?.onBackPressed()
          }
//      .setNegativeButtonBackground(R.drawable.btn_gray)
          .setNegativeButtonTextColor(getMyColor(R.color.white))
          .setNegativeButton(getMyString(R.string.cancel)) {
            it.dismiss()
          }
          .create()

      }
    })

	  val app = activity?.application as? MyApplication
	  if (app != null && !app.checkedAppGlobalAnnouncement) {
		  viewModel.getAnnouncement()
		  app.checkedAppGlobalAnnouncement = true
	  }
  }
  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.initAllServices()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
	  super.onViewCreated(view, savedInstanceState)

	  viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.showLoading.collect {
					if (it) {
						hideKeyboard()
						showLoading()
					}else {
						hideLoading()
					}
				}
			}
	  }

	  /*viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.appGlobalResponse.collect { resource ->
					if (resource is Resource.Success) {
						val response = resource.value.data

						if (response != null) {
							Log.v("TAG", "TAG MA-123 -> $response")

							val app = activity?.application as? MyApplication
							Log.v("TAG", "TAG MA-123 -> ${app?.showedAppGlobalAnnouncement}")
							if (app != null && app.showedAppGlobalAnnouncement) {
								return@collect
							}else {
								app?.showedAppGlobalAnnouncement = true
							}

							binding.root.post {
								findNavController().navigate(
									HomeFragmentDirections.actionHomeFragmentToDestAppGlobalAnnouncementDialog(
										response.title.orEmpty(),
										response.description.orEmpty(),
										response.file.orEmpty(),
									)
								)
							}
						}
					}
				}
			}
	  }*/

	  lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.CREATED) {
				viewModel.responseAnnouncement.collect { resource ->
					val response = (resource as? Resource.Success)?.value?.data ?: return@collect

					val app = activity?.application as? MyApplication
						if (app != null && app.showedAppGlobalAnnouncement) {
						return@collect
					}else {
						app?.showedAppGlobalAnnouncement = true
					}

					binding.root.post {
						if (findNavController().currentDestination?.id == R.id.home_fragment) {
							findNavController().navigateSafely(
								HomeFragmentDirections.actionHomeFragmentToDestAnnouncementDialog(
									response.image.orEmpty()
								)
							)
						}
					}
				}
			}
	  }

	  /*val app = activity?.application as? MyApplication
	  if (app != null && !app.checkedAppGlobalAnnouncement) {
		  viewModel.getAppGlobalAnnouncement()
		  app.checkedAppGlobalAnnouncement = true
	  }*/

    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
      if(bundle.containsKey(Constants.SORT_BY) && bundle.containsKey(Constants.TYPE) && bundle.getString(Constants.TYPE) == FilterDialog.CHAT.toString()) {
        Log.d(TAG, "CHAT: WORKING")
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCommetChatFragment())
//        findNavController().navigate(NavHomeDirections.actionHomeFragmentToCommetChatFragment())
//        activityViewModel.chatSelectedResult(bundle.getInt(Constants.SORT_BY))
      }
    }
  }

  private val TAG = "HomeFragment"

  override
  fun setupObservers() {
    binding.swipeRefresh.setOnRefreshListener {
      viewModel.getStories()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.homeResponse.collect {
        when (it) {
          Resource.Loading -> {
            //hideKeyboard()
	          //showLoading()
          }
          is Resource.Success -> {
	          //hideLoading()

	          /*kotlin.runCatching {
		          it.value.data.suggestions.firstOrNull { it.store.id }
	          }*/

            val hr = it.value.data.copy(
              categoryAds = ArrayList(it.value.data.categoryAds.map { ca ->
                ca.copy(name = "${resources.getString(R.string.advertisements)} ${ca.name}")
              })
            )
            hr.categoryAds.forEach {
              it.showMore.categoryId = it.id
            }

            updateList(hr)
            viewModel.updateList(hr)
            activity?.intent?.let { it1 -> viewModel.checkDeepLink(it1,binding.root) }
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
	        else -> {}
        }
      }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.storiesResponse
        .collect {
          if (it is Resource.Success) {
            binding.swipeRefresh.isRefreshing = false
            if(it.value.data.size > 0) {
              Log.d(TAG, "setupObservers: HERE STOREIS")
              val storeisSeen = ArrayList<Store>()
              val storeisNotSeen = ArrayList<Store>()
              var isSeen: Boolean
              it.value.data.forEach {
                isSeen = false
                it.stories.forEach { storyItem ->
                  if(storyItem.isSeen && !isSeen)
                    isSeen = true
                }
                if(isSeen) storeisSeen.add(it)
                else storeisNotSeen.add(it)
              }
              it.value.data.clear()
              it.value.data.addAll(storeisNotSeen)
              it.value.data.addAll(storeisSeen)
              viewModel.storiesAdapter.storiesPaginate.list.clear()
              viewModel.storiesAdapter.storiesPaginate.list.addAll(it.value.data)
              val store = Store()
              store.stories.add(StoryItem(name = getString(R.string.show_more), isFirst = true))
              it.value.data.add(0, store)
              viewModel.updateStories(it.value.data)
              ListHelper.addStories(it.value.data)
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

    if (data.mostPopularAds.isNotEmpty()) {
      val categoryAdvertisement = CategoryAdvertisement()
      categoryAdvertisement.name = resources.getString(R.string.most_popular_ads)
      categoryAdvertisement.type = 2
      categoryAdvertisement.advertisements.addAll(data.mostPopularAds)
      data.categoryAds.add(0, categoryAdvertisement)
    }
    if (data.suggestions.isNotEmpty()) {
      val categoryAdvertisement = CategoryAdvertisement()
      categoryAdvertisement.name = resources.getString(R.string.suggestions_ads_for_you)
      categoryAdvertisement.type = 1
      categoryAdvertisement.advertisements.addAll(data.suggestions)
      data.categoryAds.add(0, categoryAdvertisement)
    }
    //hey I'm HERE
  }

  override fun onValuesChanged(minValue: Float, maxValue: Float) {
    Log.d(TAG, "onValuesChanged: THERE $minValue , $maxValue")
  }

  override fun onValuesChanged(minValue: Int, maxValue: Int) {
    Log.d(TAG, "onValuesChanged: HERE $minValue , $maxValue")
  }

  var firstLoad = true
  override fun onResume() {
    super.onResume()
//    viewModel.adsHomeAdapter.updateFavourite()
//    viewModel.adsHomeAdapter.checkBlockStore()
//
//    viewModel.storeAdapter.checkFollowingStore()
//    viewModel.storeAdapter.checkBlockStore()
//
//    viewModel.followingsStoresAdapter.checkFollowingStore()
//    viewModel.followingsStoresAdapter.checkBlockStore()
//
//    viewModel.storiesAdapter.viewedStores()
//    viewModel.storiesAdapter.checkBlockStore()
//    viewModel.notifyAdapters()
    if(!firstLoad) binding.swipeRefresh.isRefreshing = true
    else{
      firstLoad = false
      viewModel.isRefresh = true
    }
    viewModel.callService()
  }

  override fun onRefresh() {
    Log.d(TAG, "onRefresh: WORKED")
//    firstLoad = false
//    viewModel.isRefresh = true
//    viewModel.getStories()
  }

}