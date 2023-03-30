package grand.app.moon.presentation.notfication.viewmodel

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.settings.models.NotificationData
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.goToAdvDetailsCheckIfMyAdv
import grand.app.moon.extensions.navToMyAdvDetails
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
	  val context = v.context ?: return

	  MyLogger.e("aa -> ch 6")
    if(model.notify_type == 2) {
			v.findFragmentOrNull<NotificationFragment>()?.viewModel?.apply {
				userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
					context,
					v.findNavController(),
					model.action_by_id,
					false
				)
			}
	    /*v.findNavController().navigate(
		    R.id.nav_ads, bundleOf(
			    "id" to model.action_by_id,
			    "type" to 2
		    )
	    )*/
	    /*if (context.isLogin() && data.user?.id == data.storeId) {
		    v.findNavController().navToMyAdvDetails(model.action_by_id)
	    }else {
		    v.findNavController().navigate(
			    R.id.nav_ads, bundleOf(
				    "id" to model.action_by_id,
				    "type" to 2
			    )
		    )
	    }*/
    }
//      }
//    }

  }
}