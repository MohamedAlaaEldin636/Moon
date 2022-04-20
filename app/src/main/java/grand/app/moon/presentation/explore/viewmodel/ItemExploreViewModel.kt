package grand.app.moon.presentation.explore.viewmodel

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.ExploreListFragmentDirections

class ItemExploreViewModel constructor(val model: Explore, val position: Int,val user : User) : BaseViewModel() {

  private  val TAG = "ItemExploreViewModel"
  init {
    Log.d(TAG, ": ${user.image}")
  }
  val submitEventEvent = MutableLiveData<String>()
  fun follow(v: View) {
    submitEventEvent.value = Constants.FOLLOW
  }

  fun fav(v: View) {
    Log.d(TAG, "fav: ")
    submitEventEvent.value = Constants.FAVOURITE
  }

  fun likes(v: View) {
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
    Log.d(TAG, "share: ")
    submitEventEvent.value = Constants.SHARE
  }

  fun allComments(v: View){
    Log.d(TAG, "allComments: ${user.name}")
    Log.d(TAG, "allComments: ${user.image}")
    if(user.id == 0) v.context.startActivity(Intent(v.context,AuthActivity::class.java))
    else if (user.image.isEmpty() || user.name.isEmpty()) {
      showInfo(v.context,v.context.resources.getString(R.string.please_complete_your_profile))
      v.findNavController().navigate(ExploreListFragmentDirections.actionExploreListFragmentToProfileFragment2())
    }else {
      submitEventEvent.value = Constants.COMMENTS
      v.findNavController().navigate(ExploreListFragmentDirections.actionExploreListFragmentToCommentsListFragment(model.id,model.comments))
    }
  }
}