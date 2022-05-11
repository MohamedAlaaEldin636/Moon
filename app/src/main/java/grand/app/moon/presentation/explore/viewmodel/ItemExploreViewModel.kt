package grand.app.moon.presentation.explore.viewmodel

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.ExploreListFragment
import grand.app.moon.presentation.explore.ExploreListFragmentDirections
import grand.app.moon.presentation.store.views.StoreDetailsFragment
import grand.app.moon.presentation.store.views.StoreDetailsFragmentDirections

class ItemExploreViewModel constructor(val model: Explore, val position: Int, val user: User) :
  BaseViewModel() {

  private val TAG = "ItemExploreViewModel"

  init {
    Log.d(TAG, ": ${user.image}")
  }

  val submitEventEvent = MutableLiveData<String>()
  fun follow(v: View) {
    if (user.id == 0) {
      v.context.startActivity(Intent(v.context, AuthActivity::class.java))
      return
    }
    val fragment = v.findFragment<ExploreListFragment>()
    fragment.viewModel.adapter.position = position
    fragment.viewModel.follow()

  }

  fun fav(v: View) {
    if (user.id == 0) {
      v.context.startActivity(Intent(v.context, AuthActivity::class.java))
      return
    }
    val fragment = v.findFragment<ExploreListFragment>()
    fragment.viewModel.adapter.position = position
    fragment.viewModel.fav()
  }

  fun likes(v: View) {
    if (user.id == 0) {
      v.context.startActivity(Intent(v.context, AuthActivity::class.java))
      return
    }
    v.findNavController().navigate(
      R.id.userListFragment,
      bundleOf(
        "explore_id" to model.id,
        "title" to v.resources.getString(R.string.this_was_liked_by_users, model.likes),
        Constants.TabBarText to v.resources.getString(R.string.likes)
      ), Constants.NAVIGATION_OPTIONS
    )
    submitEventEvent.value = Constants.LIKES
  }

  fun share(v: View) {
    val fragment = v.findFragment<ExploreListFragment>()
    fragment.viewModel.adapter.position = position
    fragment.share()
  }

  fun allComments(v: View) {
    if (user.id == 0) v.context.startActivity(Intent(v.context, AuthActivity::class.java))
    else if (user.image.isEmpty() || user.name.isEmpty()) {
      showInfo(v.context, v.context.resources.getString(R.string.please_complete_your_profile))
      v.findNavController()
        .navigate(ExploreListFragmentDirections.actionExploreListFragmentToProfileFragment2())
    } else {
      submitEventEvent.value = Constants.COMMENTS
      v.findNavController().navigate(
        ExploreListFragmentDirections.actionExploreListFragmentToCommentsListFragment(
          model.id,
          model.comments,
          position
        )
      )
    }
  }

  fun click(v: View) {
    val f = v.findFragment<Fragment>()
    when (f) {
      is ExploreListFragment -> {
        val action = if (model.mimeType.contains(Constants.VIDEO))
          ExploreListFragmentDirections.actionExploreListFragmentToVideoFragment(model.file)
        else ExploreListFragmentDirections.actionExploreListFragmentToZoomFragment(model.file)
        v.findNavController().navigate(action)
      }
      is StoreDetailsFragment -> {
        val action = if (model.mimeType.contains(Constants.VIDEO))
          StoreDetailsFragmentDirections.actionStoreDetailsFragmentToVideoFragment(model.file)
        else StoreDetailsFragmentDirections.actionStoreDetailsFragmentToZoomFragment2(model.file)
        v.findNavController().navigate(action)
      }
    }
  }
}