package com.structure.base_mvvm.presentation.home

import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseActivity
import com.structure.base_mvvm.presentation.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var nav: NavController

  override
  fun getLayoutId() = R.layout.activity_home

  override
  fun setUpBottomNavigation() {
    setUpBottomNavigationWithGraphs()
  }

  private fun setUpBottomNavigationWithGraphs() {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.fragment_host_container) as NavHostFragment
    nav = navHostFragment.navController
    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.home_fragment,
        R.id.teachersFragment,
        R.id.testsFragment,
        R.id.accountFragment,
        R.id.contact_fragment,
        R.id.about_fragment,
      ),
      binding.root,
    )
    binding.toolbar.setupWithNavController(nav, appBarConfiguration)
    binding.bottomNavigationView.setupWithNavController(nav)
    binding.navigationView.setupWithNavController(nav)
    navChangeListener()
  }

  private fun navChangeListener() {
//    nav.addOnDestinationChangedListener { _, destination, _ ->
//      if (destination.id == R.id.home_fragment || destination.id == R.id.teachersFragment || destination.id == R.id.testsFragment || destination.id == R.id.accountFragment) {
//        binding.toolbar.visibility = View.VISIBLE
//        binding.bottomNavigationView.visibility = View.VISIBLE
//        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//      } else {
    binding.toolbar.setBackgroundColor(getColor(R.color.transparent))
//        binding.bottomNavigationView.visibility = View.GONE
//        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//      }
//    }

  }

  override fun onNavigateUp(): Boolean {
    return nav.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return item.onNavDestinationSelected(nav) || super.onOptionsItemSelected(item)
  }
}