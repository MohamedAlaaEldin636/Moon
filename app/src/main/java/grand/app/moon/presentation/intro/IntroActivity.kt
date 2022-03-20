package grand.app.moon.presentation.intro

import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseActivity
import com.structure.base_mvvm.databinding.ActivityIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

  override
  fun getLayoutId() = R.layout.activity_intro
}