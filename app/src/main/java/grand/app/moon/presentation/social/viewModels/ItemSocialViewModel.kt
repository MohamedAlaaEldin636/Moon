package grand.app.moon.presentation.social.viewModels

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import grand.app.moon.R
import grand.app.moon.domain.settings.models.NotificationData
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.contactUs.ContactUsFragmentDirections
import grand.app.moon.presentation.notfication.NotificationFragment

class ItemSocialViewModel constructor(val model: SettingsData) : BaseViewModel() {

  /*
  const NOTIFICATION_TYPE = [
        'Admin'         => 1,
        'Store'         => 2,
        'All'           => 3,
        'Advertisement' => 4,
    ];
   */
  fun submit(v: View) {
    v.findNavController().navigate(
      ContactUsFragmentDirections.actionContactUsFragmentToWebFragment(
        model.title,
        model.content
      )
    )
  }
}