package com.structure.base_mvvm.presentation.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.IdRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseActivity
import com.structure.base_mvvm.presentation.base.extensions.setupWithNavController
import com.structure.base_mvvm.presentation.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
  private var isReceiverRegistered = false

  companion object {
    const val ACTION_OPEN_SPECIFIC_PAGE = "ACTION_OPEN_SPECIFIC_PAGE"
    const val TAB_ID = "TAB_ID"
  }

  override
  fun getLayoutId() = R.layout.activity_home
  override fun setUpViews() {
    setSupportActionBar(binding.toolbar)
  }

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

  private fun setUpBottomNavigationWithGraphs() {
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
    navController.value?.let { binding.navigationView.setupWithNavController(it) }
  }

  private fun navigateToSpecificTab(@IdRes tabID: Int) {
    binding.bottomNavigationView.selectedItemId = tabID
  }

  override
  fun setUpNavigationDrawer() {

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