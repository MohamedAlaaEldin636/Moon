package grand.app.moon.presentation.intro

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import grand.app.moon.R
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.ActivityIntro2Binding
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.splash.MABaseActivity

@AndroidEntryPoint
class IntroActivity : MABaseActivity<ActivityIntro2Binding>() {

  override fun getLayoutId() = R.layout.activity_intro2

}