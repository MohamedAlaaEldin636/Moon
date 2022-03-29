package grand.app.moon.presentation.myAccount

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentMyAccountBinding
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.more.MoreItem
import grand.app.moon.presentation.more.SettingsFragmentDirections
import java.util.ArrayList

@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>() {


  private val viewModel: MyAccountViewModel by viewModels()

  private val TAG = "MyAccountFragment"

  override
  fun getLayoutId() = R.layout.fragment_my_account

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    setData()
  }

  val list = ArrayList<MoreItem>()
  fun setData() {
    setList()
    viewModel.moreAdapter.clickEvent.observe(this, {
      Log.d(TAG, "setBindingVariables: here")
      if (it.id is String) {
        when (it.id) {
          Constants.LOGIN -> startActivity(Intent(activity, AuthActivity::class.java))
          Constants.LOGOUT -> {
            Log.d(TAG, "setData: logout")
            viewModel.userLocalUseCase.clearUser()
            viewModel.isLogin = false
            setList()
          }
        }
      }
    })
  }

  private fun setList() {
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
    }
    viewModel.moreAdapter.differ.submitList(list)
    viewModel.moreAdapter.notifyDataSetChanged()

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

  override fun onResume() {
    super.onResume()
    viewModel.checkLogin()
    setList()
  }

}