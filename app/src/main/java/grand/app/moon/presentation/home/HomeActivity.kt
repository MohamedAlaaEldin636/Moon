package grand.app.moon.presentation.home

import android.Manifest
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.facebook.FacebookSdk
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import grand.app.moon.R
import grand.app.moon.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.NavHomeDirections
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.core.extenstions.hide
import grand.app.moon.core.extenstions.show
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import grand.app.moon.appMoonHelper.FilterDialog

import com.onesignal.OneSignal
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.extensions.*
import grand.app.moon.helpers.update.ImmediateUpdateActivity
import grand.app.moon.presentation.splash.MABaseActivity

//private var CAUSE_NAV_UP = false

@AndroidEntryPoint
class HomeActivity : MABaseActivity<ActivityHomeBinding>(), PermissionsHandler.Listener {

  var filePath: ValueCallback<Array<Uri>>? = null
  private lateinit var appBarConfiguration: AppBarConfiguration
  lateinit var nav: NavController
  private val viewModel: HomeViewModel by viewModels()

	var immediateUpdateActivity: ImmediateUpdateActivity? = null

	lateinit var handler: PermissionsHandler

	private val broadcastReceiverIncrementNotificationsCount = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			kotlin.runCatching {
				viewModel.notificationsCount.postValue(viewModel.notificationsCount.value.orZero().inc())
			}
		}
	}

  override fun getLayoutId() = R.layout.activity_home

	override fun onAllPermissionsAccepted() {
		nav.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.qr.code",
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		handler = PermissionsHandler(
			this,
			lifecycle,
			this,
			listOf(Manifest.permission.CAMERA),
			this
		)

    super.onCreate(savedInstanceState)

	  if (true || savedInstanceState == null) {
		  setUpBottomNavigation()
		  //setUpNavigationDrawer()
	  }
	  initFacebook()
	  //setUpViews()

    immediateUpdateActivity = ImmediateUpdateActivity(this)

	  if (this::nav.isInitialized) {
      nav.currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(Constants.BUNDLE)
        ?.observe(this) { result ->
          Log.d(TAG, "onCreate: DONE HERE")
        }
    }

	  MyLogger.e("gggggggggggggggggggggggggg -> 5 ${kotlin.runCatching { nav.currentDestination?.id == R.id.dest_add_story }.getOrElse { it }}")

		viewModel.notificationsCount.observe(this) {
			if (binding.icNotification.isVisible && it.orZero() > 0) {
				binding.notificationTextView.show()
			}else {
				binding.notificationTextView.hide()
			}
		}
  }

	private fun initFacebook() {
		if (!FacebookSdk.isInitialized()) {
			FacebookSdk.setApplicationId("802733460697057")
			FacebookSdk.sdkInitialize(applicationContext)
			//setAutoLogAppEventsEnabled(true)
			FacebookSdk.fullyInitialize()
			FacebookSdk.setAutoInitEnabled(true)
			FacebookSdk.setAdvertiserIDCollectionEnabled(true)
		}
	}

	fun setUpBottomNavigation() {
		setUpBottomNavigationWithGraphs()
	}

  fun setUpViews() {
    OneSignal.setNotificationOpenedHandler { result ->
      val actionId = result.action.actionId
      Log.d(TAG, "setUpViews: $actionId")
    }
  }

  private fun setUpBottomNavigationWithGraphs() {
    binding.viewModel = viewModel
    Log.d(TAG, "setUpBottomNavigationWithGraphs: START")
    if (intent.action != null && intent.data != null) {
      Log.d(TAG, "setUpBottomNavigationWithGraphs: YES")
    }

    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.fragment_host_container) as NavHostFragment
    nav = navHostFragment.findNavController()

	  appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.storyFragment,
        R.id.addStoreFragment,
        R.id.home_fragment,
        R.id.dest_home2,
        R.id.settings_fragment,
	      R.id.dest_more,
        R.id.myAccountFragment,
        //R.id.mapFragment,
        R.id.dest_my_ads,
        R.id.dest_home_explore,
        R.id.exploreFragment
      )
    )
    setSupportActionBar(binding.toolbar)

    viewModel.clickEvent.observe(this) {
	    Log.d(TAG, "setUpBottomNavigationWithGraphs: $it")
	    when (it) {
		    Constants.LOGIN_REQUIRED -> startActivity(Intent(this, AuthActivity::class.java))
		    Constants.STORES -> {
			    val uri = Uri.Builder()
				    .scheme("storeList")
				    .authority("grand.app.moon.store.List")
				    .appendPath(getString(R.string.show_stores))
				    .appendPath("1")
				    .appendPath("-1")
				    .build()
			    val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
			    nav.navigate(request)
		    }
		    Constants.CHAT_LIST -> {
			    Log.d(TAG, "setUpBottomNavigationWithGraphs: HERE")
			    nav.navigate(HomeFragmentDirections.toFilterSortDialog(-1, FilterDialog.CHAT))
//          nav.navigate(HomeFragmentDirections.actionHomeFragmentToCommetChatFragment())
		    }
		    Constants.GO_TO_MAP -> {
			    Log.d(TAG, "GO TO MAP")
			    nav.navigate(R.id.mapFragment)
//          nav.navigate(HomeFragmentDirections.actionHomeFragmentToCommetChatFragment())
		    }
		    Constants.NOTIFICATION -> {
			    viewModel.notificationsCount.value = 0

//          if (viewModel.isLoggin)
			    nav.navigate(NavHomeDirections.moveToNotification())
//          else
//            startActivity(Intent(this, AuthActivity::class.java))
		    }
	    }
    }

	  nav.addOnDestinationChangedListener { controller, destination, arguments ->
		  viewModel.showBarCode.value = destination.id == R.id.storeListFragment || destination.id == R.id.dest_all_stores
		  viewModel.showExploreSubsectionSearch.value = destination.id == R.id.dest_home_explore_subsection

		  resetTexts()
      binding.icNotificationFilter.hide()
      when (destination.id) {
        R.id.storyFragment -> {
          hideTopBarControls()
          supportActionBar?.hide()
        }
	      R.id.dest_add_advertisement, R.id.dest_my_adv_details, R.id.dest_other_adv_details, R.id.dest_other_store_details, R.id.dest_story_player -> {
					hideTopBarControls()
		      hideAllToolbar()
		      binding.bottomNavigationView.hide()
				}
		    R.id.mapFragment -> {
			    hideTopBarControls()
			    hideAllToolbar()
			    binding.bottomNavigationView.hide()
					/*
					showTopBarControls()
          setStoreImage(false)
//          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
          binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
          binding.tvHomeTitle.text = destination.label
          showText()
					 */
				}
        R.id.addStoreFragment -> {
          binding.bottomNavigationView.show()
          showTopBarControls()
          binding.imgHomeBottomBar.setImageResource(R.drawable.btn_add)
        }
        R.id.home_fragment, R.id.dest_home2 -> {
          binding.bottomNavigationView.show()
          showTopBarControls()
          setStoreImage(false)
//          showImage()
        }
        R.id.notificationFragment -> {
          binding.toolbar.title =
            if (arguments != null && arguments.containsKey(Constants.TabBarText)) arguments.getString(
              Constants.TabBarText
            ) else destination.label
          hideTopBarControls()
          showText()
          binding.tvHomeTitle.text = destination.label
          if (isLogin()) binding.icNotificationFilter.show()
        }
        R.id.myAccountFragment -> {
          setStoreImage(false)
//          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
          binding.tvHomeTitle.text = destination.label
//          if (!viewModel.userLocalUseCase.isLoggin()) startActivity(
//            Intent(
//              this,
//              AuthActivity::class.java
//            )
//          )
          showTopBarControls()
          showText()
        }
        R.id.exploreFragment, R.id.dest_my_ads, R.id.dest_home_explore -> {
          showTopBarControls()
          setStoreImage(false)
//          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
          binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
          binding.tvHomeTitle.text = destination.label
          showText()
        }
        R.id.webFragment -> {
          setStoreImage(false)
//          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
          binding.tvHomeTitle.text = destination.label
          hideTopBarControls()
          binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
          binding.bottomNavigationView.show()
          binding.imgHomeBottomBar.show()
          binding.tvHomeTitle.text = arguments?.getString(Constants.TabBarText)
          showText()
//          binding.toolbar.hide()
        }
        R.id.settings_fragment, R.id.discoverFragment/*, R.id.dest_home_explore*/, R.id.nav_settings, R.id.dest_more -> {
          setStoreImage(false)
//          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
          binding.tvHomeTitle.text = destination.label

          showTopBarControls()
          showText()

        }
        R.id.adsDetailsFragment, R.id.storeDetailsFragment, R.id.countriesFragment3, R.id.fragment_confirm_code2, R.id.webFragment2,
        R.id.zoomPagerFragment -> {
          hideTopBarControls()
          hideAllToolbar()
        }
        else -> {
          Log.d(TAG, "setUpBottomNavigationWithGraphs: ${destination.label}")
          binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite))
          if (destination.id == R.id.dest_add_adv_final_page) {
	          val res = if (arguments?.getString("jsonResponseMyAdvDetails").isNullOrEmpty()) {
							R.string.advertisement_addition
	          }else {
							R.string.edit_adv
	          }

	          MyLogger.e("ad details cycle -> args -> ${getString(res)}")

	          binding.toolbar.title = getString(res)
          }else if (destination.id == R.id.dest_all_ads_of_category || destination.id == R.id.dest_ads_sort_dialog) {
						// Do nothing.
					}else if (arguments != null && arguments.containsKey(Constants.TabBarText)) {
	          binding.toolbar.title = arguments.getString(Constants.TabBarText)
					}else {
	          binding.toolbar.title = destination.label
					}
          hideTopBarControls()
          showText()
        }

      }
    }
    binding.imgHomeBottomBar.setOnClickListener {
	    if (isLogin()) {
				val user = viewModel.userLocalUseCase()

		    handleRetryAbleActionCancellable(
			    action = {
				    viewModel.homeUseCase.checkAvailableAdvertisementsAndShopInfo(user.isStore.orFalse())
			    }
		    ) { item ->
			    if (user.isStore == true) {
				    if (item.shopInfoIsNotCompleted) {
					    nav.currentBackStackEntry?.savedStateHandle?.remove<String>(AppConsts.NavController.GSON_KEY)
					    observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean>(nav) {
						    nav.currentBackStackEntry?.savedStateHandle?.remove<String>(AppConsts.NavController.GSON_KEY)

						    nav.navigateDeepLinkWithOptions(
							    "fragment-dest",
							    "grand.app.moon.dest.add.adv.categories.list"
						    )
					    }

					    nav.navigateDeepLinkWithOptions(
						    "fragment-dest",
						    "grand.app.moon.dest.create.store"
					    )
				    }else {
					    if (item.availableAdsCount > 0) {
						    // Add Adv
						    nav.navigateDeepLinkWithOptions(
							    "fragment-dest",
							    "grand.app.moon.dest.add.adv.categories.list"
						    )
					    }else {
						    // Renew current package or subscribe to a new package
						    nav.currentBackStackEntry?.savedStateHandle?.remove<String>(AppConsts.NavController.GSON_KEY)
						    observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean>(nav) {
							    nav.currentBackStackEntry?.savedStateHandle?.remove<String>(AppConsts.NavController.GSON_KEY)

							    handleRetryAbleActionCancellable(
								    action = {
									    viewModel.homeUseCase.checkAvailableAdvertisements()
								    }
							    ) { availableCount ->
								    if (availableCount > 0) {
									    nav.navigateDeepLinkWithOptions(
										    "fragment-dest",
										    "grand.app.moon.dest.add.adv.categories.list"
									    )
								    }else {
									    nav.navigateDeepLinkWithOptions(
										    "fragment-dest",
										    "grand.app.moon.dest.become.shop.packages"
									    )
								    }
							    }
						    }

						    nav.navigateDeepLinkWithOptions(
							    "fragment-dest",
							    "grand.app.moon.dest.my.become.shop.package"
						    )
					    }
				    }
			    }else {
				    nav.navigate(NavHomeDirections.actionGlobalDestAddAdvertisement(item.availableAdsCount))
			    }
		    }
	    }else {
		    startActivity(Intent(this, AuthActivity::class.java))
	    }
    }
    binding.icMenu.setOnClickListener {
	    if (true) {
		    AllStoresFragment.launch(
			    nav,
			    null,
			    getString(R.string.stores_879)
		    )

		    return@setOnClickListener
	    }

      val uri = Uri.Builder()
        .scheme("storeList")
        .authority("grand.app.moon.store.List")
        .appendPath(getString(R.string.stores))
        .appendPath((-1).toString())
        .appendPath("-1")
        .build()
      val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
      nav.navigate(request)
    }

    binding.bottomNavigationView.setupWithNavController(nav)
    binding.bottomNavigationView.setOnItemSelectedListener { item ->
	    if (item.itemId == R.id.dest_my_ads && !isLogin()) {
		    startActivity(Intent(this, AuthActivity::class.java))

		    false
	    }else {
		    NavigationUI.onNavDestinationSelected(item, nav)
	    }
    }

    binding.toolbar.setupWithNavController(nav, appBarConfiguration)

    binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

//    binding.bottomNavigationView.setOnItemSelectedListener { item ->
//      val id = item.itemId
//      when (id) {
//        R.id.exploreFragment -> binding.bottomNavigationView.selectedItemId = R.id.exploreFragment
//        R.id.nav_account -> binding.bottomNavigationView.selectedItemId = R.id.nav_account
//      }
//      true
//    }
//    checkDeepLink()
    initStoreBtn()
  }

  fun initStoreBtn() {
    val menu = binding.bottomNavigationView.menu
    viewModel.initIsStoreUser()
    if (!viewModel.browserHelper.isUser()) {
      menu.getItem(2).title = getString(R.string.my_store)
      setStoreImage(false)
    }
  }

  fun goHomePage(){
    binding.bottomNavigationView.selectedItemId = R.id.dest_home2//home_fragment
  }

  private fun setStoreImage(isActive: Boolean) {
    if (isActive) {
      if (viewModel.browserHelper.isUser()) {
        binding.imgHomeBottomBar.setImageResource(R.drawable.btn_add)
      } else {
        binding.imgHomeBottomBar.setImageResource(R.drawable.btn_add)
      }
    } else {
      if (viewModel.browserHelper.isUser()) {
        binding.imgHomeBottomBar.setImageResource(R.drawable.btn_add)
      } else {
        binding.imgHomeBottomBar.setImageResource(R.drawable.btn_add)
      }
    }
  }

    val TAG = "HomeActivity"


    private fun resetTexts() {
      binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
//    binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite))
      binding.tvHomeTitle.text = ""
      binding.toolbar.title = ""
    }

    private fun hideTopBarControls() {
      binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
      binding.imgHomeBottomBar.hide()
      binding.bottomNavigationView.hide()
      binding.imgMoonLogo.hide()
      binding.icNotification.hide()
      binding.notificationTextView.hide()
      binding.menuItemMap.hide()
      binding.icChat.hide()
      binding.icMenu.hide()
      binding.toolbar.show()
    }

    private fun showTopBarControls() {
      binding.imgHomeBottomBar.show()
//    binding.toolbar.background = ContextCompat.getDrawable(this, R.drawable.ic_curve)
      binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
      binding.bottomNavigationView.show()
      binding.imgMoonLogo.show()
      binding.icNotification.show()
	    if (viewModel.notificationsCount.value.orZero() > 0) {
		    binding.notificationTextView.show()
	    }else {
		    binding.notificationTextView.hide()
	    }
      binding.menuItemMap.show()
      binding.icChat.show()
      binding.icMenu.show()
      binding.toolbar.show()
    }

    private fun showImage() {
      binding.imgMoonLogo.show()
//    binding.tvHomeTitle.hide()
      setStoreImage(true)
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

    override fun onResume() {
      super.onResume()

      updateAuto(immediateUpdateActivity!!)

	    registerReceiver(
		    broadcastReceiverIncrementNotificationsCount,
		    IntentFilter(NotificationsUtils.BROADCAST_INTENT_KEY_INCREMENT_NOTIFICATIONS_COUNT)
			)
    }

	override fun onPause() {
		unregisterReceiver(broadcastReceiverIncrementNotificationsCount)

		super.onPause()
	}

	fun updateAuto(immediateUpdateActivity: ImmediateUpdateActivity){
		immediateUpdateActivity.getAppUpdateManager()!!.appUpdateInfo.addOnSuccessListener { it ->
			if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
				try {
					immediateUpdateActivity.getAppUpdateManager()!!
						.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, 381)
				} catch (e: IntentSender.SendIntentException) {
					e.printStackTrace()
				}
			}
		}
	}

}