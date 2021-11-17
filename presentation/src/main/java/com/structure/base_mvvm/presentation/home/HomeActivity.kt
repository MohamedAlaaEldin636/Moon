package com.structure.base_mvvm.presentation.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.auth.log_in.LogInFragmentDirections
import com.structure.base_mvvm.presentation.base.BaseActivity
import com.structure.base_mvvm.presentation.base.extensions.navigateSafe
import com.structure.base_mvvm.presentation.base.extensions.setupWithNavController
import com.structure.base_mvvm.presentation.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(){
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var nav: NavController

  private var isReceiverRegistered = false

  companion object {
    const val ACTION_OPEN_SPECIFIC_PAGE = "ACTION_OPEN_SPECIFIC_PAGE"
    const val TAB_ID = "TAB_ID"
  }

  override
  fun getLayoutId() = R.layout.activity_home

  override
  fun onResume() {
    super.onResume()
    registerOpenSpecificTabReceiver()
  }

  private val openSpecificTabReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override
    fun onReceive(context: Context, intent: Intent) {
      navigateToSpecificTab(intent.getIntExtra(TAB_ID, 0))
    }
  }

  private fun registerOpenSpecificTabReceiver() {
    if (!isReceiverRegistered) {
      LocalBroadcastManager.getInstance(this)
        .registerReceiver(
          openSpecificTabReceiver,
          IntentFilter(ACTION_OPEN_SPECIFIC_PAGE)
        )
      isReceiverRegistered = true
    }
  }

  override
  fun setUpBottomNavigation() {
    setUpBottomNavigationWithGraphs()
  }

  fun setUpBottomNavigationWithGraphs() {
    val graphIds = listOf(
      R.navigation.nav_home,
      R.navigation.nav_search,
      R.navigation.nav_account
    )

    val controller = binding.bottomNavigationView.setupWithNavController(
      graphIds,
      supportFragmentManager,
      R.id.fragment_host_container,
      intent
    )
    navController = controller
//    val navHostFragment =
//      supportFragmentManager.findFragmentById(R.id.fragment_host_container) as NavHostFragment
//    nav = navHostFragment.findNavController()
//    appBarConfiguration = AppBarConfiguration(
//      setOf(R.id.home_fragment),
//      binding.root
//    )
//
//    setSupportActionBar(binding.toolbar)
//    setupActionBarWithNavController(nav, appBarConfiguration)
////    binding.bottomNavigationView.setupWithNavController(nav)
//    binding.navigationView.setupWithNavController(nav)
//    binding.navigationView.setNavigationItemSelectedListener(this)
  }

  private fun navigateToSpecificTab(@IdRes tabID: Int) {
    binding.bottomNavigationView.selectedItemId = tabID
  }

  override
  fun onDestroy() {
    unregisterOpenSpecificTabReceiver()
    super.onDestroy()
  }

  private fun unregisterOpenSpecificTabReceiver() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(openSpecificTabReceiver)
  }

}