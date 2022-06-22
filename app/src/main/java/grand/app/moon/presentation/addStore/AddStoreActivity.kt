package grand.app.moon.presentation.addStore

import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStoreActivity : BaseActivity<ActivityIntroBinding>() {

  override
  fun getLayoutId() = R.layout.activity_add_store

}