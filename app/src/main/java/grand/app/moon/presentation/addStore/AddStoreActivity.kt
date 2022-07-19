package grand.app.moon.presentation.addStore

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.databinding.ActivityIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.ActivityAddStoreBinding

@AndroidEntryPoint
class AddStoreActivity : BaseActivity<ActivityAddStoreBinding>() {

  override
  fun getLayoutId() = R.layout.activity_add_store

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.icHome.setOnClickListener {
      finish()
    }
  }

  override fun onResume() {
    super.onResume()
  }
}