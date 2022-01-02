package com.structure.base_mvvm.presentation.teachers.home

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseActivity
import com.structure.base_mvvm.presentation.databinding.ActivityTeacherHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherHomeActivity : BaseActivity<ActivityTeacherHomeBinding>() {
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var nav: NavController

  override
  fun getLayoutId() = R.layout.activity_teacher_home

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
        R.id.teacherHomeFragment,
        R.id.teacherGroupsFragment,
        R.id.teacherAccountFragment,
        R.id.moreFragment,
        R.id.contact_fragment,
        R.id.about_fragment,
      )
    )
    setSupportActionBar(binding.toolbar)
    setupActionBarWithNavController(nav, appBarConfiguration)
    binding.bottomNavigationView.setupWithNavController(nav)
    navChangeListener()
  }

  private fun navChangeListener() {
    nav.addOnDestinationChangedListener { _, destination, _ ->
      if (destination.id == R.id.teacherHomeFragment || destination.id == R.id.teacherGroupsFragment || destination.id == R.id.teacherAccountFragment || destination.id == R.id.moreFragment) {
        binding.toolbar.visibility = View.VISIBLE
        binding.bottomNavigationView.visibility = View.VISIBLE
      } else {
        binding.toolbar.setBackgroundColor(getColor(R.color.transparent))
        binding.bottomNavigationView.visibility = View.GONE
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