package grand.app.moon.presentation.home

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.extensions.hide
import grand.app.moon.presentation.base.extensions.show
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
      setOf(R.id.home_fragment,R.id.settings_fragment , R.id.myAccountFragment, R.id.mapFragment , R.id.discoverFragment)
    )
    setSupportActionBar(binding.toolbar)
    binding.toolbar.setupWithNavController(nav, appBarConfiguration)

    nav.addOnDestinationChangedListener { controller, destination, arguments ->
      binding.tvHomeTitle.text = destination.label
      when(destination.id) {
        R.id.home_fragment -> {
          binding.bottomNavigationView.show()
          showTopBarControls()
          showImage()
        }
        R.id.myAccountFragment -> {
          if(!viewModel.userLocalUseCase.isLoggin()) startActivity(Intent(this, AuthActivity::class.java))
          showTopBarControls()
          showText()
        }
        R.id.settings_fragment , R.id.mapFragment, R.id.discoverFragment ->{
          showTopBarControls()
          showText()
        }
        else -> {
          hideTopBarControls()
          showText()
        }

      }
    }

    binding.bottomNavigationView.setupWithNavController(nav)
  }

  private fun hideTopBarControls() {
    binding.toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
    binding.bottomNavigationView.hide()
    binding.imgMoonLogo.hide()
    binding.icNotification.hide()
    binding.icChat.hide()
    binding.icMenu.hide()
  }

  private fun showTopBarControls() {
    binding.toolbar.background = ContextCompat.getDrawable(this,R.drawable.ic_curve)
    binding.bottomNavigationView.show()
    binding.imgMoonLogo.show()
    binding.icNotification.show()
    binding.icChat.show()
    binding.icMenu.show()
  }

  private fun showImage() {
    binding.imgMoonLogo.show()
    binding.tvHomeTitle.hide()
  }

  private fun showText() {
    binding.imgMoonLogo.hide()
    binding.tvHomeTitle.show()

  }


  override fun onSupportNavigateUp(): Boolean {
    return nav.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

}