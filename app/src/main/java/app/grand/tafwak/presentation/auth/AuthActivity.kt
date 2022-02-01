package app.grand.tafwak.presentation.auth

import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseActivity
import com.structure.base_mvvm.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

  override
  fun getLayoutId() = R.layout.activity_auth
}