package grand.app.moon.presentation.more

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.shared.core.extensions.rateApp
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.core.MyApplication
import grand.app.moon.databinding.FragmentSettingsBinding
import grand.app.moon.extensions.General
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.intro.IntroActivity
import java.util.ArrayList

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {


  private val viewModel: SettingsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_settings

  override
  fun setBindingVariables() {
    Log.d(TAG, "setBindingVariables: HERE")
    binding.viewModel = viewModel
    val moreItems = ArrayList<MoreItem>().also { list ->
      list.add(
        MoreItem(
          getString(R.string.about_moon),
          getMyDrawable(R.drawable.ic_about_moon_settings),
          "https://${viewModel.countryIso}.souqmoon.com/${viewModel.lang}/mobile/about"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.contact_us),
          getMyDrawable(R.drawable.ic_contact_settings),
          Constants.CONTACT
        )
      )
      list.add(
        MoreItem(
          getString(R.string.terms),
          getMyDrawable(R.drawable.ic_terms_settings),
          "https://${viewModel.countryIso}.souqmoon.com/${viewModel.lang}/mobile/terms"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.help),
          getMyDrawable(R.drawable.ic_help_settings),
          "https://${viewModel.countryIso}.souqmoon.com/${viewModel.lang}/mobile/help"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.change_language),
          getMyDrawable(R.drawable.ic_language_settings),
          Constants.LANG
        )
      )
      list.add(
        MoreItem(
          getString(R.string.change_country),
          getMyDrawable(R.drawable.ic_country_settings),
          Constants.COUNTRY
        )
      )
//      list.add(
//        MoreItem(
//          getString(R.string.social_media),
//          getMyDrawable(R.drawable.ic_social_settings),
//          Constants.SOCIAL
//        )
//      )

      list.add(
        MoreItem(
          getString(R.string.share_app),
          getMyDrawable(R.drawable.ic_share_settings),
          Constants.SHARE,
        )
      )


      list.add(
        MoreItem(
          getString(R.string.rate_app),
          getMyDrawable(R.drawable.ic_rate_app),
          Constants.GOOGLE_PLAY,
          "https://play.google.com/store/apps/details?id=${activity?.applicationContext?.packageName}"
        )
      )
    }
    list.forEach {
      Log.d(TAG, "setBindingVariables: ${it.id}")
    }
    viewModel.moreAdapter.differ.submitList(moreItems)
    setData()

    viewModel.moreAdapter.clickEvent.observe(this) {
	    Log.d(TAG, "setBindingVariables: here")
	    if (it?.id is String) {
		    when (it.id) {
			    Constants.GOOGLE_PLAY -> rateApp()
			    Constants.SHARE -> {
				    val intent = Intent(Intent.ACTION_SEND)
				    intent.type = "text/plain"

				    val messageDisplay =
					    "https://play.google.com/store/apps/details?id=${activity?.applicationContext?.packageName}"
				    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
				    intent.putExtra(Intent.EXTRA_TEXT, messageDisplay)
				    context?.startActivity(
					    Intent.createChooser(
						    intent,
						    resources.getString(R.string.share)
					    )
				    )
			    }
			    else -> {
				    Log.d(TAG, "setBindingVariables: ${it.id}")
				    navigateSafe(
					    SettingsFragmentDirections.actionSettingsFragmentToWebFragment(
						    it.title!!,
						    it.id.toString()
					    )
				    )
			    }
		    }
		    Log.d(TAG, "setBindingVariables: ")
//        openUrl(it.id.toString())
	    } else {
		    if (it?.id != -1) {
//          Log.d(TAG, "setBindingVariables: ${it.id}")
			    when (it?.id) {
				    Constants.CONTACT -> {
					    navigateSafe(SettingsFragmentDirections.actionSettingsFragmentToContactUsFragment())
				    }
				    Constants.LANG -> {
					    navigateSafe(
						    SettingsFragmentDirections.actionMoreFragmentToLanguageFragment2(
							    Constants.MORE
						    )
					    )
				    }
				    Constants.COUNTRY -> {
					    navigateSafe(
						    SettingsFragmentDirections.actionMoreFragmentToCountriesFragment3(
							    Constants.MORE
						    )
					    )
				    }
				    Constants.SOCIAL -> {
					    navigateSafe(
						    SettingsFragmentDirections.actionSettingsFragmentToSocialFragment()
					    )
				    }
				    else -> {
				    }
			    }
		    }
	    }
    }
  }

  private val TAG = "MoreFragment"

  override fun onStop() {
    super.onStop()
    viewModel.moreAdapter.clickEvent.value = MoreItem(icon = null, id = -1)
  }

  val list = ArrayList<MoreItem>()
  private fun setListProfile() {
    list.clear()
    list.also { list ->
      when (viewModel.isLogin) {
        true -> {
          addMyAccountSettings()
        }
        else -> {
          list.add(
            MoreItem(
              getString(R.string.log_in),
              getMyDrawable(R.drawable.ic_login),
              Constants.LOGIN
            )
          )
        }
      }

	    // todo del later just for current sprint testing purpose
	    /*list.add(
		    MoreItem(
			    getString(R.string.packages_898),
			    getMyDrawable(R.drawable.ic_view),
			    Constants.PACKAGES
		    )
			)*/
	    list.add(
		    MoreItem(
			    getString(R.string.store_data),
			    getMyDrawable(R.drawable.ic_store_data_4),
			    Constants.STORE_INFO
		    )
			)
    }
    viewModel.accountAdapter.differ.submitList(list)
    viewModel.accountAdapter.notifyDataSetChanged()

  }

  private fun addMyAccountSettings() {
    list.add(
      MoreItem(
        getString(R.string.personal_info),
        getMyDrawable(R.drawable.ic_profile),
        Constants.PROFILE
      )
    )
    list.add(
      MoreItem(
        getString(R.string.last_ads_seen),
        getMyDrawable(R.drawable.ic_view),
        Constants.LAST_ADS
      )
    )
    list.add(
      MoreItem(
        getString(R.string.stores_had_been_followed),
        getMyDrawable(R.drawable.ic_login),
        Constants.STORES_FOLLOWED
      )
    )

    list.add(
      MoreItem(
        getString(R.string.last_search),
        getMyDrawable(R.drawable.ic_last_search),
        Constants.LAST_SEARCH
      )
    )
    list.add(
      MoreItem(
        getString(R.string.favourite),
        getMyDrawable(R.drawable.ic_favourite_user),
        Constants.FAVOURITE
      )
    )
    list.add(
      MoreItem(
        getString(R.string.stores_had_been_blocked),
        getMyDrawable(R.drawable.ic_stores_block),
        Constants.STORES_BLOCKED
      )
    )
    list.add(
      MoreItem(
        getString(R.string.logout),
        getMyDrawable(R.drawable.ic_login),
        Constants.LOGOUT
      )
    )
  }

  fun setData() {
    setListProfile()
    viewModel.accountAdapter.clickEvent.observe(this) {
//      Log.d(TAG, "setBindingVariables: here $it")
	    if (it?.id is String) {
		    Log.d(TAG, "setData: ASDASSADDSA")
		    when (it.id) {
			    Constants.PACKAGES -> {
				    if (viewModel.userLocalUseCase().isStore == true) {
					    findNavController().navigateDeepLinkWithOptions(
						    "fragment-dest",
						    "grand.app.moon.dest.become.shop.packages"
					    )
				    }else {
							// Remove below later
							General.TODO("Only for shop account")
				    }
					}
			    Constants.STORE_INFO -> {
				    if (viewModel.userLocalUseCase().isStore == true) {
					    findNavController().navigateDeepLinkWithOptions(
						    "fragment-dest",
						    "grand.app.moon.dest_store.full.data"
					    )
				    }else {
					    // Remove below later
					    General.TODO("Only for shop account")
				    }
					}
			    Constants.PROFILE -> navigateSafe(SettingsFragmentDirections.actionMyAccountFragmentToProfileFragment())
			    Constants.LOGIN -> openLoginActivity()
			    Constants.LAST_ADS -> navigateSafe(
				    SettingsFragmentDirections.actionMyAccountFragmentToAdsListFragment(
					    2,
					    resources.getString(R.string.last_ads_seen)
				    )
			    )
			    Constants.FAVOURITE -> navigateSafe(
				    SettingsFragmentDirections.actionMyAccountFragmentToAdsListFragment(
					    1,
					    resources.getString(R.string.favourite)
				    )
			    )
			    Constants.LAST_SEARCH -> navigateSafe(
				    SettingsFragmentDirections.actionMyAccountFragmentToAdsListFragment(
					    5,
					    resources.getString(R.string.last_search)
				    )
			    )
			    Constants.STORES_FOLLOWED -> {
				    navigateSafe(
					    SettingsFragmentDirections.actionMyAccountFragmentToStoreFollowedListFragment()
				    )
			    }
			    Constants.STORES_BLOCKED -> {
				    findNavController().navigate(SettingsFragmentDirections.actionMyAccountFragmentToStoreBlockListFragment())
			    }
			    Constants.LOGOUT -> {
				    viewModel.logout()
				    logout()
				    openActivityAndClearStack(HomeActivity::class.java)
			    }
		    }
	    }
    }
  }

  override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume: WORKED")
  }


}