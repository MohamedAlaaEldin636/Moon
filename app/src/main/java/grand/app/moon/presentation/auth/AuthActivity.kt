package grand.app.moon.presentation.auth

import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.presentation.splash.MABaseActivity

@AndroidEntryPoint
class AuthActivity : MABaseActivity<ActivityAuthBinding>() {

  override fun getLayoutId() = R.layout.activity_auth

}