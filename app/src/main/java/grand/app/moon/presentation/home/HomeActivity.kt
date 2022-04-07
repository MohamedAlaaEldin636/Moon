package grand.app.moon.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_components.users.user_list.CometChatUserList
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.NavHomeDirections
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.extensions.hide
import grand.app.moon.presentation.base.extensions.invisible
import grand.app.moon.presentation.base.extensions.show
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.viewModels.HomeViewModel

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var nav: NavController
  private val viewModel: HomeViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.activity_home

  override
  fun setUpBottomNavigation() {
    setUpBottomNavigationWithGraphs()
  }

  private fun setUpBottomNavigationWithGraphs() {
    binding.viewModel = viewModel
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.fragment_host_container) as NavHostFragment
    nav = navHostFragment.findNavController()
    appBarConfiguration = AppBarConfiguration(
      setOf(R.id.home_fragment,R.id.settings_fragment , R.id.myAccountFragment, R.id.mapFragment , R.id.exploreFragment)
    )
    setSupportActionBar(binding.toolbar)
    binding.toolbar.setupWithNavController(nav, appBarConfiguration)

   viewModel.clickEvent.observe(this, {
      if (it == Constants.LOGIN_REQUIRED) startActivity(Intent(this, AuthActivity::class.java))
    })


    nav.addOnDestinationChangedListener { controller, destination, arguments ->
      resetTexts()
      when(destination.id) {
        R.id.home_fragment -> {
          binding.bottomNavigationView.show()
          showTopBarControls()
          showImage()
        }
        R.id.myAccountFragment -> {
          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_not_active)
          binding.tvHomeTitle.text = destination.label
          if(!viewModel.userLocalUseCase.isLoggin()) startActivity(Intent(this, AuthActivity::class.java))
          showTopBarControls()
          showText()
        }
        R.id.exploreFragment , R.id.mapFragment ->{
          showTopBarControls()
          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_not_active)
          binding.toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
          binding.tvHomeTitle.text = destination.label
          showText()
        }
        R.id.settings_fragment , R.id.discoverFragment  ->{
          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_not_active)
          binding.tvHomeTitle.text = destination.label
          showTopBarControls()
          showText()
        }
        R.id.adsDetailsFragment ->{
          hideTopBarControls()
          hideAllToolbar()
        }
        else -> {
          binding.toolbar.title = destination.label
          hideTopBarControls()
          showText()
        }

      }
    }
    binding.imgHomeBottomBar.setOnClickListener {

      nav.navigate(NavHomeDirections.moveToHome())
    }

    binding.bottomNavigationView.setupWithNavController(nav)
    binding.toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
  }

  private  val TAG = "HomeActivity"


  private fun resetTexts(){
    binding.toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorWhite))
    binding.tvHomeTitle.text = ""
    binding.toolbar.title = ""
  }

  private fun hideTopBarControls() {
    binding.toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
    binding.imgHomeBottomBar.hide()
    binding.bottomNavigationView.hide()
    binding.imgMoonLogo.hide()
    binding.icNotification.hide()
    binding.icChat.hide()
    binding.icMenu.hide()
    binding.toolbar.show()
  }

  private fun showTopBarControls() {
    binding.imgHomeBottomBar.show()
    binding.toolbar.background = ContextCompat.getDrawable(this,R.drawable.ic_curve)
    binding.bottomNavigationView.show()
    binding.imgMoonLogo.show()
    binding.icNotification.show()
    binding.icChat.show()
    binding.icMenu.show()
    binding.toolbar.show()
  }

  private fun showImage() {
    binding.imgMoonLogo.show()
//    binding.tvHomeTitle.hide()
    binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
    binding.toolbar.show()
  }

  private fun showText() {
    binding.imgMoonLogo.hide()
    binding.tvHomeTitle.show()
    binding.toolbar.show()
  }

  private fun hideAllToolbar() {
    binding.imgMoonLogo.hide()
    binding.tvHomeTitle.hide()
    binding.toolbar.hide()
  }


  override fun onSupportNavigateUp(): Boolean {
    return nav.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

}