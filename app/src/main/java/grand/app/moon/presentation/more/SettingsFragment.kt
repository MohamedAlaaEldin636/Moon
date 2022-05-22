package grand.app.moon.presentation.more

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.shared.core.extensions.rateApp
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.core.MyApplication
import grand.app.moon.databinding.FragmentSettingsBinding
import grand.app.moon.presentation.base.utils.Constants
import java.util.ArrayList

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {


  private val viewModel: SettingsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_settings

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    val moreItems = ArrayList<MoreItem>().also { list ->
      list.add(
        MoreItem(
          getString(R.string.about_moon),
          getMyDrawable(R.drawable.ic_about_moon_settings),
          "https://moontest.my-staff.net/${viewModel.lang}/about"
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
          "https://moontest.my-staff.net/${viewModel.lang}/terms"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.help),
          getMyDrawable(R.drawable.ic_help_settings),
          "https://moontest.my-staff.net/${viewModel.lang}/faq/help"
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
      list.add(
        MoreItem(
          getString(R.string.social_media),
          getMyDrawable(R.drawable.ic_social_settings),
          Constants.SOCIAL
        )
      )
      list.add(
        MoreItem(
          getString(R.string.register_as_provider),
          getMyDrawable(R.drawable.ic_register_as_store),
          "https://moontest.my-staff.net/store/register"
        )
      )

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
    viewModel.moreAdapter.differ.submitList(moreItems)

    viewModel.moreAdapter.clickEvent.observe(this,{
      Log.d(TAG, "setBindingVariables: here")
      if(it.id is String){
        when(it.id){
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
            navigateSafe(SettingsFragmentDirections.actionSettingsFragmentToWebFragment(it.title!!, it.id.toString())) }
        }
        Log.d(TAG, "setBindingVariables: ")
//        openUrl(it.id.toString())
      }else {
        if(it.id != -1) {
          Log.d(TAG, "setBindingVariables: ${it.id}")
          when (it.id) {
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
    })
  }

  private val TAG = "MoreFragment"

  override fun onStop() {
    super.onStop()
    viewModel.moreAdapter.clickEvent.value = MoreItem(icon = null,id = -1)
  }


}