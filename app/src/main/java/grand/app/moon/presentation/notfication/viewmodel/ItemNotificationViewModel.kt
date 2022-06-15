package grand.app.moon.presentation.notfication.viewmodel

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.settings.models.NotificationData
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.notfication.NotificationFragment

class ItemNotificationViewModel constructor(val model: NotificationData) : BaseViewModel() {

  /*
  const NOTIFICATION_TYPE = [
        'Admin'         => 1,
        'Store'         => 2,
        'All'           => 3,
        'Advertisement' => 4,
    ];
   */
  fun submit(v: View) {
//    when (model.notify_type) {
//      2 -> v.findNavController().navigate(
//        R.id.nav_store,
//        bundleOf(
//          "id" to model.action_by_id,
//          "type" to 3
//        ), Constants.NAVIGATION_OPTIONS
//      )
//      4 -> {
    if(model.notify_type == 2)
        v.findNavController().navigate(
          R.id.nav_ads, bundleOf(
            "id" to model.action_by_id,
            "type" to 2
          )
        )
//      }
//    }

  }
}