package grand.app.moon.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.NavHomeDirections
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.extensions.hide
import grand.app.moon.presentation.base.extensions.show
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import com.zeugmasolutions.localehelper.LocaleHelper
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.core.MyApplication
import java.util.*
import com.onesignal.OSNotificationOpenedResult

import com.onesignal.OneSignal
import com.onesignal.OneSignal.OSNotificationOpenedHandler
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.helpers.update.ImmediateUpdateActivity
import grand.app.moon.presentation.addStore.AddStoreActivity
import grand.app.moon.presentation.base.extensions.openActivity
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
  var filePath: ValueCallback<Array<Uri>>? = null
  private lateinit var appBarConfiguration: AppBarConfiguration
  lateinit var nav: NavController
  private val viewModel: HomeViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.activity_home

  override
  fun setUpBottomNavigation() {
    setUpBottomNavigationWithGraphs()
  }

  var immediateUpdateActivity: ImmediateUpdateActivity? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    immediateUpdateActivity = ImmediateUpdateActivity(this)
//    val lang = viewModel.accountRepository.getKeyFromLocal(Constants.LANGUAGE)
//    if (lang.isEmpty()) {
//      LocaleHelper.setLocale(this, Locale(lang))
//    }

    if (this::nav.isInitialized) {
      nav.currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(Constants.BUNDLE)
        ?.observe(this) { result ->
          // Do something with the result.
          Log.d(TAG, "onCreate: DONE HERE")
        }
    }
//    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
//      if(bundle.containsKey(Constants.SORT_BY)) {
//        when(bundle.getInt(Constants.SORT_BY)){
//          1 -> activityViewModel.toChatList(binding.root)
//          else -> activityViewModel.toWhatsappList(binding.root)
//        }
//      }
//    }
  }

  override fun setUpViews() {
    super.setUpViews()

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
        R.id.settings_fragment,
        R.id.myAccountFragment,
        R.id.mapFragment,
        R.id.exploreFragment
      )
    )
    setSupportActionBar(binding.toolbar)

    viewModel.clickEvent.observe(this, {
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
        Constants.NOTIFICATION -> {
//          if (viewModel.isLoggin)
          nav.navigate(NavHomeDirections.moveToNotification())
//          else
//            startActivity(Intent(this, AuthActivity::class.java))
        }
      }
    })



    nav.addOnDestinationChangedListener { controller, destination, arguments ->

      resetTexts()
      binding.icNotificationFilter.hide()
      when (destination.id) {
        R.id.storyFragment -> {
          hideTopBarControls()
          supportActionBar?.hide()
        }
        R.id.addStoreFragment -> {
          binding.bottomNavigationView.show()
          showTopBarControls()
          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
        }
        R.id.home_fragment -> {
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
        R.id.exploreFragment, R.id.mapFragment -> {
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
        R.id.settings_fragment, R.id.discoverFragment, R.id.nav_settings -> {
          setStoreImage(false)
//          binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
          binding.tvHomeTitle.text = destination.label

          showTopBarControls()
          showText()

        }
        R.id.adsDetailsFragment, R.id.storeDetailsFragment, R.id.countriesFragment3, R.id.fragment_confirm_code2, R.id.webFragment2, R.id.zoomPagerFragment-> {
          hideTopBarControls()
          hideAllToolbar()
        }

        else -> {
          Log.d(TAG, "setUpBottomNavigationWithGraphs: ${destination.label}")
          binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite))
          binding.toolbar.title =
            if (arguments != null && arguments.containsKey(Constants.TabBarText)) arguments.getString(
              Constants.TabBarText
            ) else destination.label
          hideTopBarControls()
          showText()
        }

      }
    }
    binding.imgHomeBottomBar.setOnClickListener {
      showImage()
      if (viewModel.browserHelper.isUser())
        binding.bottomNavigationView.selectedItemId = R.id.addStoreFragment
      else {
        openActivity(AddStoreActivity::class.java)
      }
    }
    binding.icMenu.setOnClickListener {
      val uri = Uri.Builder()
        .scheme("storeList")
        .authority("grand.app.moon.store.List")
        .appendPath(getString(R.string.stores))
        .appendPath(3.toString())
        .appendPath("-1")
        .build()
      val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
      nav.navigate(request)
    }

    binding.bottomNavigationView.setupWithNavController(nav)

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
    binding.bottomNavigationView.selectedItemId = R.id.home_fragment
  }

  private fun setStoreImage(isActive: Boolean) {
    if (isActive) {
      if (viewModel.browserHelper.isUser()) {
        binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_active)
      } else {
        binding.imgHomeBottomBar.setImageResource(R.drawable.ic_my_store)
      }
    } else {
      if (viewModel.browserHelper.isUser()) {
        binding.imgHomeBottomBar.setImageResource(R.drawable.ic_home_circle_not_active)
      } else {
        binding.imgHomeBottomBar.setImageResource(R.drawable.ic_my_store)
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
    }

  }