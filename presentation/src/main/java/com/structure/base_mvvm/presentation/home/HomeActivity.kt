package com.structure.base_mvvm.presentation.home

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
    nav = navHostFragment.findNavController()
    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.home_fragment,
        R.id.teachersFragment,
        R.id.testsFragment,
        R.id.accountFragment,
        R.id.contact_fragment,
        R.id.suggestions_fragment,
        R.id.about_fragment,
        R.id.privacy_fragment,
        R.id.terms_fragment,
      ),
      binding.root,
    )
    setSupportActionBar(binding.toolbar)
    setupActionBarWithNavController(nav, appBarConfiguration)
    binding.bottomNavigationView.setupWithNavController(nav)
    binding.navigationView.setupWithNavController(nav)
    navChangeListener()
  }

  private fun navChangeListener() {
    nav.addOnDestinationChangedListener { _, destination, _ ->
      if (destination.id == R.id.home_fragment || destination.id == R.id.teachersFragment || destination.id == R.id.testsFragment || destination.id == R.id.accountFragment || destination.id == R.id.contact_fragment || destination.id == R.id.suggestions_fragment) {
        binding.toolbar.visibility = View.VISIBLE
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
      } else {
        binding.toolbar.visibility = View.GONE
        binding.bottomNavigationView.visibility = View.GONE
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
      }
    }

  }

  override fun onSupportNavigateUp(): Boolean {
    return nav.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.top_app_bar, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return item.onNavDestinationSelected(nav) || super.onOptionsItemSelected(item)
  }

}