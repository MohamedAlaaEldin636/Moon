package grand.app.moon.presentation.more

import android.util.Log
import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentMoreBinding
import grand.app.moon.presentation.base.utils.Constants
import java.util.ArrayList

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {


  private val viewModel: MoreViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_more

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    val moreItems = ArrayList<MoreItem>().also { list ->
      list.add(
        MoreItem(
          getString(R.string.about_moon),
          getMyDrawable(R.drawable.ic_about_moon_settings),
          "https://google.com"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.contact_us),
          getMyDrawable(R.drawable.ic_contact_settings),
          1
        )
      )
      list.add(
        MoreItem(
          getString(R.string.terms),
          getMyDrawable(R.drawable.ic_terms_settings),
          "https://google.com"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.help),
          getMyDrawable(R.drawable.ic_help_settings),
          "https://google.com"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.change_language),
          getMyDrawable(R.drawable.ic_language_settings),
          2
        )
      )
      list.add(
        MoreItem(
          getString(R.string.change_country),
          getMyDrawable(R.drawable.ic_country_settings),
          3
        )
      )
      list.add(
        MoreItem(
          getString(R.string.social_media),
          getMyDrawable(R.drawable.ic_social_settings),
          "https://google.com"
        )
      )
      list.add(
        MoreItem(
          getString(R.string.register_as_provider),
          getMyDrawable(R.drawable.ic_register_as_store),
          "https://google.com"
        )
      )

      list.add(
        MoreItem(
          getString(R.string.share_app),
          getMyDrawable(R.drawable.ic_share_settings),
          "https://google.com"
        )
      )


      list.add(
        MoreItem(
          getString(R.string.rate_app),
          getMyDrawable(R.drawable.ic_rate_app),
          "https://google.com"
        )
      )
    }
    viewModel.moreAdapter.differ.submitList(moreItems)

    viewModel.moreAdapter.clickEvent.observe(this,{
      Log.d(TAG, "setBindingVariables: here")
      if(it.id is String){
        Log.d(TAG, "setBindingVariables: ")
        openUrl(it.id.toString())
      }else {
        Log.d(TAG, "setBindingVariables: ${it.id}")
        when(it.id){
          1 -> {

          }
          2 -> {
            navigateSafe(MoreFragmentDirections.actionMoreFragmentToLanguageFragment2(Constants.MORE))
          }
          3 -> {
            navigateSafe(MoreFragmentDirections.actionMoreFragmentToCountriesFragment3(Constants.MORE))
          }
          else -> {}
        }
      }
    })
  }

  private val TAG = "MoreFragment"

}