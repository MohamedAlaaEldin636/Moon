package grand.app.moon.presentation.intro

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.splash.MABaseActivity

@AndroidEntryPoint
class IntroActivity : MABaseActivity<ActivityIntroBinding>() {

  override fun getLayoutId() = R.layout.activity_intro

	override fun onCreate(savedInstanceState: Bundle?) {
		MyLogger.e("udhiewudhweiudwidh -1")

		super.onCreate(savedInstanceState)

		MyLogger.e("udhiewudhweiudwidh 0")
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
		val nav = navHostFragment.findNavController()
		MyLogger.e("udhiewudhweiudwidh 1")
		nav.addOnDestinationChangedListener { controller, destination, arguments ->
			MyLogger.e("udhiewudhweiudwidh")
			MyLogger.e("udhiewudhweiudwidh $destination")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		MyLogger.e("udhiewudhweiudwidh -1")

		super.onCreate(savedInstanceState, persistentState)

		MyLogger.e("udhiewudhweiudwidh 0")
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
		val nav = navHostFragment.findNavController()
		MyLogger.e("udhiewudhweiudwidh 1")
		nav.addOnDestinationChangedListener { controller, destination, arguments ->
			MyLogger.e("udhiewudhweiudwidh")
			MyLogger.e("udhiewudhweiudwidh $destination")
		}
	}

}