package grand.app.moon.presentation.home

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI
import com.cometchat.pro.uikit.ui_components.groups.admin_moderator_list.CometChatAdminModeratorListActivity
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_components.messages.threaded_message_list.CometChatThreadMessageListActivity
import com.cometchat.pro.uikit.ui_components.users.user_details.CometChatUserDetailScreenActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
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