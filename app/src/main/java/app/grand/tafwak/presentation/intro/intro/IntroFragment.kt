package app.grand.tafwak.presentation.intro.intro

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView.BufferType.SPANNABLE
import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import com.structure.base_mvvm.R.color
import app.grand.tafwak.presentation.auth.AuthActivity
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.getMyColor
import app.grand.tafwak.presentation.base.extensions.openActivityAndClearStack
import com.structure.base_mvvm.databinding.FragmentIntroBinding
import app.grand.tafwak.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>() {

  private val viewModel: IntroViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_intro

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
  }


  override
  fun setupObservers() {
    viewModel.openLogIn.observe(this) {
      viewModel.setFirstTime(false)
      openLogIn()
    }
  }

  private fun openLogIn() {
    openActivityAndClearStack(AuthActivity::class.java)
  }

  private fun openHome() {
    openActivityAndClearStack(HomeActivity::class.java)
  }
}